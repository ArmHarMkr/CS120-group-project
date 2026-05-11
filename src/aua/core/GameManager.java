package aua.core;

import aua.utils.StringUtil;
import aua.core.exceptions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class GameManager {
    private static final String separator = "-";
    private static final int width = 21;
    private static final int height = 21;
    private GameMap map;
    private Player player;
    private Point playerPosition;
    private boolean isRunning;
    private String message;
    private boolean inShop;
    private Shop shop;

    public GameManager(String[] storedGame){
        this();
    }

    public GameManager(){
        this.player = new Player();
        this.map = new GameMap(width, height);
        addStartingPlants();
        this.playerPosition = findStartingPosition();
        this.isRunning = true;
        this.message = "Use roads to move around the map.";
        this.inShop = false;
        this.shop =new Shop();
        addProductToShop();
    }


    private void addProductToShop(){
        shop.addProduct(new Plant("Carrot Seed",     4,  new Product("Carrot",      35, 2)));
        shop.addProduct(new Plant("Tomato Seed",     6,  new Product("Tomato",      12, 3)));
        shop.addProduct(new Plant("Potato Seed",     5,  new Product("Potato",      23, 4)));
        shop.addProduct(new Plant("Corn Seed",       8,  new Product("Corn",        12, 5)));
        shop.addProduct(new Plant("Pumpkin Seed",    10, new Product("Pumpkin",     22, 7)));
        shop.addProduct(new Plant("Watermelon Seed", 12, new Product("Watermelon",  77, 9)));
        shop.addProduct(new Plant("Strawberry Seed", 7,  new Product("Strawberry",  2, 6)));
        shop.addProduct(new Plant("Sunflower Seed",  5,  new Product("Sunflower",   3, 3)));
    }

    public int getShopSize(){
        return shop.getSize();
    }

    public int getInventorySize(){
        return player.getInventoryItems().length;
    }



    public static GameManager load() throws IOException, MalformedStringException, NumberFormatException {
        GameManager reconstructedGameManager = new GameManager();

        StorageManager storageManager = new StorageManager();

        String[] storedStrings = storageManager.load();

        String playerString = storedStrings[0];
        String inventoryString = storedStrings[1];


        reconstructedGameManager.player = new Player(playerString, inventoryString);

        String mapMetadata = storedStrings[3];

        int mapHeight = storedStrings.length - 4;
        String[] mapTileData = new String[mapHeight];
        int index = 0;

        for (int i = 4; i < storedStrings.length; i++) {
            mapTileData[index] = storedStrings[i];
            index++;
        }

        reconstructedGameManager.map = new GameMap(mapMetadata, mapTileData);

        String shopString = storedStrings[2];

        reconstructedGameManager.shop = new Shop(shopString);


        String[] parsedPlayerString = StringUtil.parseDelimitedString(playerString);
        if(parsedPlayerString[0].equals("PLAYER")){
            String[] coordinates = StringUtil.parseDelimitedString(parsedPlayerString[1], StringUtil.separator);

            int playerX =  Integer.parseInt(coordinates[0]);
            int playerY =  Integer.parseInt(coordinates[1]);

            reconstructedGameManager.playerPosition = new Point(playerX, playerY);
        } else {
            throw new MalformedStringException();
        }

        return reconstructedGameManager;
    }

    public void save() throws FileNotFoundException, IOException, CloneNotSupportedException {
        String delimiter = StringUtil.defaultDelimiter;
        String separator = StringUtil.separator;

        ArrayList<String> dataStrings = new ArrayList<String>();
        String playerDataString = "PLAYER"+delimiter+playerPosition.getX()+separator+playerPosition.getY()+delimiter+player.getMoney();

        String inventoryDataString = "INVENTORY";
        Item[] inventoryItems = this.player.getInventoryItems();


        for (int i = 0; i < inventoryItems.length; i++) {
            if(inventoryItems[i] instanceof WorldObject){
                Plant plant = (Plant) inventoryItems[i];
                inventoryDataString = inventoryDataString+delimiter+plant;
            } else if(inventoryItems[i] instanceof Product) {
                Product product = (Product) inventoryItems[i];
                inventoryDataString = inventoryDataString+delimiter+product;
            }
        }

        dataStrings.add(playerDataString);
        dataStrings.add(inventoryDataString);

        dataStrings.add(this.shop.toString());

        String mapMetadata = this.map.getMapMetadata();
        dataStrings.add(mapMetadata);

        String[] mapEncoding = this.map.getMapEncoding();

        for(int i = 0; i < mapEncoding.length; i++) {
            dataStrings.add(mapEncoding[i]);
        }

        StorageManager storageManager = new StorageManager();

        //Source  https://stackoverflow.com/a/5374359/20792752
        String[] dataStringsArray = new String[dataStrings.size()];
        dataStringsArray = dataStrings.toArray(dataStringsArray);

        storageManager.save(dataStringsArray);
    }

    public void quit(){
        isRunning = false;
        message = "Thanks for playing.";
    }

    public void handleMovement(char input) throws GameActionException {
        input = Character.toLowerCase(input);
        int nextX = playerPosition.getX();
        int nextY = playerPosition.getY();

        if(input == 'w'){
            nextY--;
        } else if(input == 's'){
            nextY++;
        } else if(input == 'a'){
            nextX--;
        } else if(input == 'd'){
            nextX++;
        }
        else {
            throw new InvalidDirectionException("Unknown command.");
        }

        movePlayer(nextX, nextY);
    }

    public void selectInventoryItem(int index) throws GameActionException {
        if(player.selectItem(index)){
            Item selectedItem = player.getSelectedItem();
            message = "Selected " + selectedItem.getName() + ".";
        } else {
            throw new InvalidInventorySelectionException("No item in that inventory slot.");
        }
    }

    public void plant(char direction) throws GameActionException {
        Item selectedItem = player.getSelectedItem();

        if(!(selectedItem instanceof Plant)){
            throw new InvalidGameActionException("Select a plant from your inventory first.");
        }

        direction = Character.toLowerCase(direction);
        if(direction == ' '){
            throw new InvalidDirectionException("Choose where to plant after pressing P: Q, W, E, A, S, D, Z, or C.");
        }

        Point target = getTargetPoint(direction);
        if(target == null){
            throw new InvalidDirectionException("Choose where to plant after pressing P: Q, W, E, A, S, D, Z, or C.");
        }

        if(map.placeObject(target.getX(), target.getY(), (WorldObject) selectedItem)){
            Item plant = player.takeSelectedItem();
            this.tickAll();
            message = "Planted " + plant.getName() + ".";
        } else {
            throw new InvalidGameActionException("You can only plant on empty soil next to you.");
        }
    }

    public void collect(char direction) throws GameActionException {
        direction = Character.toLowerCase(direction);

        Point target = getTargetPoint(direction);
        if(target == null){
            throw new InvalidDirectionException("Choose where to collect after pressing H: Q, W, E, A, S, D, Z, or C.");
        }

        if(player.isInventoryFull()){
            throw new InvalidGameActionException("Inventory is full.");
        }

        Product product = map.collectPlant(target.getX(), target.getY());
        if(product == null){
            throw new InvalidGameActionException("There is no mature plant to collect there.");
        }

        player.addToInventory(product);
        this.tickAll();
        message = "Collected " + product.getName() + ".";
    }

    private Point getTargetPoint(char direction){
        int targetX = playerPosition.getX();
        int targetY = playerPosition.getY();

        if(direction == 'q'){
            targetX--;
            targetY--;
        } else if(direction == 'w'){
            targetY--;
        } else if(direction == 'e'){
            targetX++;
            targetY--;
        } else if(direction == 'a'){
            targetX--;
        } else if(direction == 's'){
            targetY++;
        } else if(direction == 'd'){
            targetX++;
        } else if(direction == 'z'){
            targetX--;
            targetY++;
        } else if(direction == 'c'){
            targetX++;
            targetY++;
        } else {
            return null;
        }

        return new Point(targetX, targetY);
    }

    public void tickAll(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if(this.map.getTile(j, i).getObject() instanceof Plant){
                    Plant plant = (Plant) this.map.getTile(j, i).getObject();
                    if(!plant.isReady()){
                        plant.grow();
                    }
                }
            }
        }
    }

    private void movePlayer(int nextX, int nextY) throws GameActionException {
        if(map.isWalkable(nextX, nextY)){
            playerPosition.setX(nextX);
            playerPosition.setY(nextY);
            this.tickAll();
            message = "Moved to (" + nextX + ", " + nextY + ").";
        } else {
            throw new InvalidGameActionException("You cannot walk there.");
        }
    }


    private Point findStartingPosition(){
        for(int i = 0; i < map.getHeight(); i++){
            for(int j = 0; j < map.getWidth(); j++){
                if(map.isWalkable(j, i)){
                    return new Point(j, i);
                }
            }
        }

        return new Point();
    }

    private void addStartingPlants(){
        player.addToInventory(new Plant("Carrot Seed", 4, new Product("Carrot", 0, 2)));
        player.addToInventory(new Plant("Carrot Seed", 4, new Product("Carrot", 0, 2)));
        player.addToInventory(new Plant("Tomato Seed", 6, new Product("Tomato", 0, 3)));
    }

    public boolean isRunning(){
        return isRunning;
    }

    public String drawMap(){
        return map.draw(playerPosition.getX(), playerPosition.getY());
    }

    public int getMapWidth(){
        return map.getWidth();
    }

    public int getMapHeight(){
        return map.getHeight();
    }

    public int getPlayerX(){
        return playerPosition.getX();
    }

    public int getPlayerY(){
        return playerPosition.getY();
    }

    public TerrainType getTerrainTypeAt(int x, int y){
        return map.getTile(x, y).getType();
    }

    public boolean hasPlantAt(int x, int y){
        return map.getTile(x, y).getObject() instanceof Plant;
    }

    public boolean isMaturePlantAt(int x, int y){
        if(!(map.getTile(x, y).getObject() instanceof Plant)){
            return false;
        }

        Plant plant = (Plant) map.getTile(x, y).getObject();
        return plant.isReady();
    }

    public int getGrowthRatioAt(int x, int y){
        if(!hasPlantAt(x,y)){
            return -1;
        }

        Plant plant = (Plant) map.getTile(x, y).getObject();
        double growthPeriod = plant.getGrowthPeriod();
        double currentGrowthPeriod = plant.getCurrentGrowth();
        int growthRatio = (int)(currentGrowthPeriod/growthPeriod*10);
        return growthRatio;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String drawInventory(){
        Item[] items = player.getInventoryItems();
        String inventoryText = "Inventory: ";

        if(items.length == 0){
            return inventoryText + "empty";
        }

        for(int i = 0; i < items.length; i++){
            if(i == player.getSelectedInventoryIndex()){
                inventoryText += "[" + (i + 1) + ":" + items[i].getName() + "] ";
            } else {
                inventoryText += (i + 1) + ":" + items[i].getName() + " ";
            }
        }

        return inventoryText;
    }

    public boolean isInShop(){
        return inShop;
    }

    private boolean isAdjacentToShop(){
        int playerX = playerPosition.getX();
        int playerY = playerPosition.getY();
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                if(i == 0 && j == 0){
                    continue;
                }
                int neighbourX = playerX + j;
                int neighbourY = playerY + i;

                if(map.isShop(neighbourX, neighbourY)){
                    return true;
                }
            }
        }
        return false;
    }

    public void enterShop() throws GameActionException {
        if(!isAdjacentToShop()){
            throw new InvalidGameActionException("You need to be next to the shop.");
        }
        inShop = true;
        message = "Welcome to the shop!";
    }

    public void exitShop(){
        inShop = false;
        message = "You left the shop.";
    }

    public String drawShop(){
        String screen = "";
        screen += "===== SHOP  =======   \n";
        screen += "Money: $" + player.getMoney() + "\n\n";
        screen += "-- SHOP STOCK --\n";
        Item[] products = shop.getProducts();
        for(int i = 0; i < products.length; i++){
            screen += (i + 1) + ": " + products[i].getName() + "  [buy: $" + products[i].getBuyPrice() + "]\n";
        }
        screen += "\n-- YOUR INVENTORY --\n";
        Item[] items = player.getInventoryItems();
        for(int i = 0; i < items.length; i++){
            screen += (i + 1) + ": " + items[i].getName();
            if(items[i] instanceof Product){
                screen += "  [sell: $" + ((Product) items[i]).getSellPrice() + "]";
            }
            screen += "\n";
        }
        screen += "\nB = buy | S = sell | X = leave shop\n";

        return screen;
    }

    public void buyItem(int index) throws GameActionException {
        Item[] items = shop.getProducts();
        if(index < 0 || index >= items.length){
            throw new InvalidInventorySelectionException("No item at that slot.");
        }
        if(player.isInventoryFull()){
            throw new InvalidGameActionException("Your inventory is full.");
        }
        if(!shop.buy(player, items[index])){
            throw new InvalidGameActionException("Not enough money. You need $" + items[index].getBuyPrice() + ".");
        }
        message = " You bought " + items[index].getName() + " for $" + items[index].getBuyPrice() + ".";
    }

    public void sellItem(int index) throws GameActionException {
        Item[] items = player.getInventoryItems();
        if(index < 0 || index >= items.length){
            throw new InvalidInventorySelectionException("No item at that slot.");
        }
        if(!(items[index] instanceof Product)){
            throw new InvalidGameActionException("You can only sell harvest, not seeds.");
        }
        player.selectItem(index);
        Product product = (Product) player.getSelectedItem();
        if(!shop.sell(player, product)){
            throw new InvalidGameActionException("Could not sell the item.");
        }
        message = "You sold " + product.getName() + " for $" + product.getSellPrice() + ".";
    }




}
