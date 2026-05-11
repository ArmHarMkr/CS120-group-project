package aua;

import aua.cli.GrandmasGardenConsole;
import aua.core.GameManager;
import aua.ui.GrandmasGardenUI;

public class Main {
    public static void main(String[] args) {
        args = new String[] {"-ui"};
        String mode = args.length > 0 ? args[0] : "-ui" ;

        try {
            GameManager gameManager = new GameManager();
            switch(mode){
                case "-ui":
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            GrandmasGardenUI ui = new GrandmasGardenUI(gameManager);
                            ui.start();
                        }
                    });
                    break;
                case "-cli":
                    GrandmasGardenConsole consoleInterface = new GrandmasGardenConsole(gameManager);
                    consoleInterface.start();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown mode: " + mode);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
