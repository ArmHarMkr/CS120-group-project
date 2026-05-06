package aua.Core;


public abstract class WorldObject implements Item {
    protected String name;

    public WorldObject(String name){
        this.name = name;
    }

    public String getName(){ return name; }
    public abstract WorldObject copy();
}
