package aua.core;


public abstract class WorldObject implements Item {
    protected String name;

    public WorldObject(String name){
        this.name = name;
    }
    public WorldObject(){
        this.name = "";
    }

    public String getName(){ return name; }
    public abstract WorldObject copy();
}
