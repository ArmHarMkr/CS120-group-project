package aua.Core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
        this.playerPosition = findStartingPosition();
        this.isRunning = true;
        this.message = "Use roads to move around the map.";
    }

    public void start(){
        Scanner scanner = new Scanner(System.in);

        while(isRunning){
            draw();
            System.out.print("Move with W/A/S/D, or Q to quit: ");
            String command = scanner.nextLine();
            update(command);
        }

    }

    public void load(){

    }

    public void save() throws FileNotFoundException, IOException {
        String delimiter = "%%";
        String separator = "-";

        ArrayList<String> dataStrings = new ArrayList<String>();
        String playerDataString = "PLAYER"+delimiter+playerPosition.getX()+separator+playerPosition.getY();

        String inventoryDataString = "INVENTORY";
        WorldObject[] inventoryWorldObjects = this.player.getInventory().getItems();

        for (int i = 0; i < inventoryWorldObjects.length; i++) {
//              Waiting for the implementation of basic get methods for plant
                inventoryDataString = inventoryDataString+delimiter+inventoryWorldObjects[i].getName();
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

    public void update(){

    }

    public void draw(){
        System.out.println(map.draw(playerPosition.getX(), playerPosition.getY()));
        System.out.println("@ player, . road, # rock");
        System.out.println(message);
    }

    private void update(String command){
        if(command == null || command.isEmpty()){
            return;
        }

        char input = Character.toLowerCase(command.charAt(0));
        int nextX = playerPosition.getX();
        int nextY = playerPosition.getY();

        if(input == 'q'){
            isRunning = false;
            message = "Thanks for playing.";
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

    private void movePlayer(int nextX, int nextY){
        if(map.isWalkable(nextX, nextY)){
            playerPosition.setX(nextX);
            playerPosition.setY(nextY);
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
}
