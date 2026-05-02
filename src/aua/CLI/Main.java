package aua.CLI;

import aua.Core.GameManager;

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.start();
        try {
            gameManager.save();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
