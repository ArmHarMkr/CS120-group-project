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

    public void save(){

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
}