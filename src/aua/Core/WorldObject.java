package aua.Core;

public abstract class WorldObject {
    protected String name;

    public WorldObject(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
