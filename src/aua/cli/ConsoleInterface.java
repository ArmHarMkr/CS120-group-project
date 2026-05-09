package aua.cli;

import aua.core.GameManager;
import aua.core.exceptions.GameActionException;

import java.util.Scanner;

public class ConsoleInterface {
    private GameManager gameManager;
    private Scanner scanner;

    public ConsoleInterface(GameManager gameManager){
        this.gameManager = gameManager;
        this.scanner = new Scanner(System.in);
    }

    public void start(){
        while(gameManager.isRunning()){
            draw();
            System.out.print("Command: ");

            if(!scanner.hasNextLine()){
                gameManager.quit();
                return;
            }

            handleCommand(scanner.nextLine().trim());
        }
    }

    private void draw(){
        System.out.println(gameManager.drawMap());
        System.out.println("@ player, . road, , soil, # rock, P plant, M mature plant");
        System.out.println("W/A/S/D move | P plant | H collect | 1-9 select item | Q quit");
        System.out.println(gameManager.drawInventory());
        System.out.println(gameManager.getMessage());
    }

    private void handleCommand(String command){
        if(command == null || command.isEmpty()){
            return;
        }

        char input = Character.toLowerCase(command.charAt(0));

        if(Character.isDigit(input)){
            gameManager.selectInventoryItem(Character.getNumericValue(input) - 1);
        } else if(input == 'q'){
            gameManager.quit();
        } else if(input == 'p'){
            handlePlanting();
        } else if(input == 'h'){
            handleCollecting();
        } else {
            gameManager.handleMovement(input);
        }
    }

    private void handlePlanting(){
        System.out.print("Plant direction (Q/W/E/A/S/D/Z/C): ");

        if(!scanner.hasNextLine()){
            gameManager.quit();
            return;
        }

        String direction = scanner.nextLine().trim();
        if(direction.isEmpty()){
            gameManager.plant(' ');
        } else {
            gameManager.plant(direction.charAt(0));
        }
    }

    private void handleCollecting(){
        System.out.print("Collect direction (Q/W/E/A/S/D/Z/C): ");

        if(!scanner.hasNextLine()){
            gameManager.quit();
            return;
        }

        String direction = scanner.nextLine().trim();
        if(direction.isEmpty()){
            gameManager.collect(' ');
        } else {
            gameManager.collect(direction.charAt(0));
        }
    }
}
