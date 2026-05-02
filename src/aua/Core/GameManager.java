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

    public void tickAll(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if(this.map.getTile(i,j).getObject() instanceof Plant){
                    Plant plant = (Plant) this.map.getTile(i,j).getObject();
                    if(!plant.isReady()){
                        plant.grow();
                    }
                }
            }
        }
    }

    private void movePlayer(int nextX, int nextY){
        if(map.isWalkable(nextX, nextY)){
            playerPosition.setX(nextX);
            playerPosition.setY(nextY);
            this.tickAll();
            message = "Moved to (" + nextX + ", " + nextY + ").";
        } else {
            message = "You cannot walk there.";
        }
    }


    private Point findStartingPosition(){
        for(int y = 0; y < map.getHeight(); y++){
            for(int x = 0; x < map.getWidth(); x++){
                if(map.isWalkable(x, y)){
                    return new Point(x, y);
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
