package aua.cli;

import aua.core.GameManager;
import aua.core.Playable;
import aua.core.exceptions.GameActionException;

import java.util.Scanner;

public class GrandmasGardenConsole implements Playable {
    private GameManager gameManager;
    private Scanner scanner;

    public GrandmasGardenConsole(GameManager gameManager){
        this.gameManager = gameManager;
        this.scanner = new Scanner(System.in);
    }

    public void start(){
        while(gameManager.isRunning()){
            if(gameManager.isInShop()){
                drawShop();
                System.out.print("Command: ");
                if(!scanner.hasNextLine()){
                    gameManager.quit();
                    return;
                }
                handleShopCommand(scanner.nextLine().trim());
            } else {
                draw();
                System.out.print("Command: ");
                if(!scanner.hasNextLine()){
                    gameManager.quit();
                    return;
                }
                handleCommand(scanner.nextLine().trim());
            }
        }
        System.out.println(gameManager.getMessage());
    }

    public void draw(){
        System.out.println(gameManager.drawMap());
        System.out.println("@ player, . road, , soil, # rock, P plant, M mature plant and S is a shop");
        System.out.println("W/A/S/D move | P plant | H collect | 1-9 select item | Q quit| E enter | X exit");
        System.out.println(gameManager.drawInventory());
        System.out.println(gameManager.getMessage());
    }

    private void handleCommand(String command){
        if(command == null || command.isEmpty()){
            return;
        }

        try {
            char input = Character.toLowerCase(command.charAt(0));

            if(Character.isDigit(input)){
                gameManager.selectInventoryItem(Character.getNumericValue(input) - 1);
            } else if(input == 'q'){
                gameManager.quit();
            } else if(input == 'p'){
                handlePlanting();
            } else if(input == 'h'){
                handleCollecting();
            } else if(input == 'e') {
                gameManager.enterShop();
            }else if(input == 'x') {
                gameManager.exitShop();
            }
            else {
                gameManager.handleMovement(input);
            }
        } catch(GameActionException exception){
            gameManager.setMessage(exception.getMessage());
        }
    }

    public void handlePlanting() throws GameActionException {
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

    public void handleCollecting() throws GameActionException {
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

    public void drawShop(){
        System.out.println(gameManager.drawShop());
        System.out.println(gameManager.getMessage());
    }

    private void handleShopCommand(String command){
        if(command == null || command.isEmpty()){
            return;
        }
        try {
            char input = Character.toLowerCase(command.charAt(0));
            if(input == 'x'){
                gameManager.exitShop();
            } else if(input == 'b'){
                System.out.print("Buy which item (1-" + gameManager.getShopSize() + "): ");
                String line = scanner.nextLine().trim();
                if(line.isEmpty()) return;
                char number = line.charAt(0);
                if(!Character.isDigit(number)){
                    gameManager.setMessage("Please enter a number.");
                    return;
                }
                gameManager.buyItem(Character.getNumericValue(number) - 1);

            } else if(input == 's'){
                System.out.print("Sell which item (1-" + gameManager.getInventorySize() + "): ");
                String line = scanner.nextLine().trim();
                if(line.isEmpty()) return;
                char number = line.charAt(0);
                if(!Character.isDigit(number)){
                    gameManager.setMessage("Please enter a number.");
                    return;
                }
                gameManager.sellItem(Character.getNumericValue(number) - 1);

            } else {
                gameManager.setMessage("B = buy | S = sell | X = leave");
            }

        } catch(GameActionException exception){
            gameManager.setMessage(exception.getMessage());
        }
    }


}
