package aua;

import aua.cli.ConsoleInterface;
import aua.core.GameManager;

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        ConsoleInterface consoleInterface = new ConsoleInterface(gameManager);
        consoleInterface.start();
    }
}
