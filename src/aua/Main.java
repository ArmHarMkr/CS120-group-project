package aua;

import aua.CLI.ConsoleInterface;
import aua.Core.GameManager;

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        ConsoleInterface consoleInterface = new ConsoleInterface(gameManager);
        consoleInterface.start();
    }
}
