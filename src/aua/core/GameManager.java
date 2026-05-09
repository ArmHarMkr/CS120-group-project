package aua.core;

import aua.core.exceptions.GameActionException;
import aua.core.exceptions.InvalidDirectionException;
import aua.core.exceptions.InvalidGameActionException;
import aua.core.exceptions.InvalidInventorySelectionException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class GameManager {
    private static final int width = 21;
    private static final int height = 21;
    private GameMap map;
    private Player player;
    private Point playerPosition;
    private boolean isRunning;
    private String message;

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
    }

    public void load(){

    }

    public void save() throws FileNotFoundException, IOException, CloneNotSupportedException {
        String delimiter = "%%";
        String separator = "-";

        ArrayList<String> dataStrings = new ArrayList<String>();
        String playerDataString = "PLAYER"+delimiter+playerPosition.getX()+separator+playerPosition.getY()+delimiter+player.getMoney();

        String inventoryDataString = "INVENTORY";
        Item[] inventoryItems = this.player.getInventoryItems();

        for (int i = 0; i < inventoryItems.length; i++) {
            if(inventoryItems[i] instanceof WorldObject){
                Plant plant = (Plant) inventoryItems[i];
                Product plantProduct = plant.getProductParameters();
                inventoryDataString = inventoryDataString+delimiter+"PLANT"+separator+plant.getName()+separator+plant.getCurrentGrowth()+separator+plant.getGrowthPeriod()+separator+plantProduct.getBuyPrice()+separator+plantProduct.getSellPrice();
            } else if(inventoryItems[i] instanceof Product) {
                Product product = (Product) inventoryItems[i];
                inventoryDataString = inventoryDataString+delimiter+"PRODUCT"+separator+product.getName()+separator+product.getBuyPrice()+separator+product.getSellPrice();
            }
        }

        dataStrings.add(playerDataString);
        dataStrings.add(inventoryDataString);

        String[] mapEncoding = this.map.getMapEncoding(delimiter);

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
        } else {
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
}
