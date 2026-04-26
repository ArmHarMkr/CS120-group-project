package aua.Core;

public class GameManager {
    private static final int width = 700;
    private static final int height = 700;
    private GameMap map;
    private Player player;

    public GameManager(String[] storedGame){

    }

    public GameManager(){
        this.player = new Player();
        this.map = new GameMap(width, height);
    }

    public void start(){

    }

    public void load(){

    }

    public void save(){

    }

    public void update(){

    }

    public void draw(){

    }
}
