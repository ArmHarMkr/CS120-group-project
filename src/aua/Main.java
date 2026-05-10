package aua;

import aua.cli.GrandmasGardenConsole;
import aua.core.GameManager;
import aua.ui.GrandmasGardenUI;

public class Main {
    public static void main(String[] args) {
//        GameManager gameManager = new GameManager();
//        GrandmasGardenConsole consoleInterface = new GrandmasGardenConsole(gameManager);
//        consoleInterface.start();
//        try{
//            gameManager.save();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        args = new String[]{"-ui"};
        String mode = args.length > 0 ? args[0] : "-ui" ;


        try {
            GameManager gameManager = GameManager.load();
            switch(mode){
                case "-ui":
                    GrandmasGardenUI ui = new GrandmasGardenUI();
                    break;
                case "-cli":
                    GrandmasGardenConsole consoleInterface = new GrandmasGardenConsole(gameManager);
                    consoleInterface.start();
                    break;
            }

            gameManager.save();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
