package aua;

import aua.cli.ConsoleInterface;
import aua.core.GameManager;

public class Main {
    public static void main(String[] args) {
//        GameManager gameManager = new GameManager();
//        ConsoleInterface consoleInterface = new ConsoleInterface(gameManager);
//        consoleInterface.start();
//        try{
//            gameManager.save();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        try {
            GameManager gameManager = GameManager.load();;
            ConsoleInterface consoleInterface = new ConsoleInterface(gameManager);
            consoleInterface.start();
            gameManager.save();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
