package aua.cli;

import aua.core.GameManager;
import aua.core.Item;
import aua.core.Plant;
import aua.core.Product;
import aua.core.Playable;
import aua.core.exceptions.GameActionException;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    public void load(){
        try{
            this.gameManager = GameManager.load();
            System.out.println("The game was successfully loaded");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void save(){
        try{
            this.gameManager.save();
            System.out.println("The game was successfully saved!");
        }catch (CloneNotSupportedException e){
            System.out.println("Error: "+e.getMessage());
        }catch (FileNotFoundException e){
            System.out.println("File Not Found: Try again and provide a valid file path");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }}

    public void draw(){
        System.out.println(gameManager.drawMap());
        System.out.println("@ player, . road, , soil, # rock, P plant, M mature plant and S is a shop");
        System.out.println("W/A/S/D move | I inventory/select | P plant selected seed | H harvest | E shop | L load | K save | Q quit");
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
                gameManager.selectInventoryItem(parseSlotNumber(command) - 1);
            } else if(input == 'q'){
                gameManager.quit();
            } else if(input == 'i'){
                handleInventorySelection();
            } else if(input == 'p'){
                handlePlanting();
            } else if(input == 'h'){
                handleCollecting();
            } else if(input == 'e') {
                gameManager.enterShop();
            }else if(input == 'x') {
                gameManager.exitShop();
            } else if(input == 'l'){
                load();
            } else if(input == 'k'){
                save();
            }
            else {
                gameManager.handleMovement(input);
            }
        } catch(GameActionException exception){
            gameManager.setMessage(exception.getMessage());
        }
    }

    private void handleInventorySelection() throws GameActionException {
        drawDetailedInventory();
        System.out.print("Select item number, or press Enter to cancel: ");

        if(!scanner.hasNextLine()){
            gameManager.quit();
            return;
        }

        String line = scanner.nextLine().trim();
        if(line.isEmpty()){
            gameManager.setMessage("Inventory selection cancelled.");
            return;
        }

        gameManager.selectInventoryItem(parseSlotNumber(line) - 1);
    }

    private void drawDetailedInventory(){
        Item[] items = gameManager.getInventoryItems();

        System.out.println();
        System.out.println("===== INVENTORY =====");
        if(items.length == 0){
            System.out.println("empty");
            return;
        }

        for(int i = 0; i < items.length; i++){
            String marker = i == gameManager.getSelectedInventoryIndex() ? " * " : "   ";
            System.out.print(marker + (i + 1) + ". " + items[i].getName());

            if(items[i] instanceof Plant){
                System.out.print(" [seed]");
            } else if(items[i] instanceof Product){
                Product product = (Product) items[i];
                System.out.print(" [harvest, sells for $" + product.getSellPrice() + "]");
            }

            System.out.println();
        }
    }

    public void handlePlanting() throws GameActionException {
        System.out.println("Planting uses the currently selected seed.");
        System.out.println(gameManager.drawInventory());
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
        System.out.print("Harvest direction (Q/W/E/A/S/D/Z/C): ");

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

    private int parseSlotNumber(String input) throws GameActionException {
        try {
            return Integer.parseInt(input);
        } catch(NumberFormatException exception){
            throw new GameActionException("Please enter a number.");
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
                gameManager.buyItem(parseSlotNumber(line) - 1);

            } else if(input == 's'){
                System.out.print("Sell which item (1-" + gameManager.getInventorySize() + "): ");
                String line = scanner.nextLine().trim();
                if(line.isEmpty()) return;
                gameManager.sellItem(parseSlotNumber(line) - 1);

            } else {
                gameManager.setMessage("B = buy | S = sell | X = leave");
            }

        } catch(GameActionException exception){
            gameManager.setMessage(exception.getMessage());
        }
    }


}
