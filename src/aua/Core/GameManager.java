package aua.Core;

import java.util.Scanner;

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

    public void start(){
        Scanner scanner = new Scanner(System.in);

        while(isRunning){
            draw();
            System.out.print("Command: ");
            if(!scanner.hasNextLine()){
                isRunning = false;
                return;
            }
            String command = scanner.nextLine().trim();
            if(command.equalsIgnoreCase("p")){
                System.out.print("Plant direction (Q/W/E/A/S/D/Z/C): ");
                if(!scanner.hasNextLine()){
                    isRunning = false;
                    return;
                }
                command += scanner.nextLine().trim();
            }
            update(command);
        }

    }

    public void load(){

    }

    public void save(){

    }

    public void update(){

    }

    public void draw(){
        System.out.println(map.draw(playerPosition.getX(), playerPosition.getY()));
        System.out.println("@ player, . road, , soil, # rock, P plant, M mature plant");
        System.out.println("W/A/S/D move | P plant around player | 1-9 select item | Q quit");
        System.out.println(drawInventory());
        System.out.println(message);
    }

    private void update(String command){
        if(command == null || command.isEmpty()){
            return;
        }

        char input = Character.toLowerCase(command.charAt(0));
        int nextX = playerPosition.getX();
        int nextY = playerPosition.getY();

        if(Character.isDigit(input)){
            selectInventoryItem(input);
            return;
        } else if(input == 'q'){
            isRunning = false;
            message = "Thanks for playing.";
            return;
        } else if(input == 'p'){
            plant(command);
            return;
        } else if(input == 'w'){
            nextY--;
        } else if(input == 's'){
            nextY++;
        } else if(input == 'a'){
            nextX--;
        } else if(input == 'd'){
            nextX++;
        } else {
            message = "Unknown command.";
            return;
        }

        movePlayer(nextX, nextY);
    }

    private void selectInventoryItem(char input){
        int index = Character.getNumericValue(input) - 1;

        if(player.selectItem(index)){
            WorldObject selectedItem = player.getSelectedItem();
            message = "Selected " + selectedItem.getName() + ".";
        } else {
            message = "No item in that inventory slot.";
        }
    }

    private void plant(String command){
        WorldObject selectedItem = player.getSelectedItem();

        if(!(selectedItem instanceof Plant)){
            message = "Select a plant from your inventory first.";
            return;
        }

        char direction = getDirection(command);
        if(direction == ' '){
            message = "Choose where to plant after pressing P: Q, W, E, A, S, D, Z, or C.";
            return;
        }

        Point target = getTargetPoint(direction);
        if(target == null){
            message = "Choose where to plant after pressing P: Q, W, E, A, S, D, Z, or C.";
            return;
        }

        if(map.placeObject(target.getX(), target.getY(), selectedItem)){
            WorldObject plant = player.takeSelectedItem();
            map.tickAll();
            message = "Planted " + plant.getName() + ".";
        } else {
            message = "You can only plant on empty soil next to you.";
        }
    }

    private char getDirection(String command){
        for(int i = 1; i < command.length(); i++){
            char letter = Character.toLowerCase(command.charAt(i));
            if(letter == 'q' || letter == 'w' || letter == 'e' || letter == 'a' ||
                    letter == 's' || letter == 'd' || letter == 'z' || letter == 'c'){
                return letter;
            }
        }

        return ' ';
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
        } else if(direction == 's'){
            targetY++;
        } else if(direction == 'a'){
            targetX--;
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

    private void movePlayer(int nextX, int nextY){
        if(map.isWalkable(nextX, nextY)){
            playerPosition.setX(nextX);
            playerPosition.setY(nextY);
            map.tickAll();
            message = "Moved to (" + nextX + ", " + nextY + ").";
        } else {
            message = "You cannot walk there.";
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
        Product carrot = new Product("Carrot", 4, 2);
        Product tomato = new Product("Tomato", 6, 3);

        player.addToInventory(new Plant("Carrot Seed", 4, carrot));
        player.addToInventory(new Plant("Carrot Seed", 4, carrot));
        player.addToInventory(new Plant("Tomato Seed", 6, tomato));
    }

    private String drawInventory(){
        Inventory inventory = player.getInventory();
        WorldObject[] items = inventory.getItems();
        String inventoryText = "Inventory: ";

        if(items.length == 0){
            return inventoryText + "empty";
        }

        for(int i = 0; i < items.length; i++){
            if(i == inventory.getSelectedIndex()){
                inventoryText += "[" + (i + 1) + ":" + items[i].getName() + "] ";
            } else {
                inventoryText += (i + 1) + ":" + items[i].getName() + " ";
            }
        }

        return inventoryText;
    }
}
