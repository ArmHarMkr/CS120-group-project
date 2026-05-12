package aua.core;


public abstract class WorldObject implements Item {
    protected String name;

    /**
     *
     * @param name
     */
    public WorldObject(String name){
        this.name = name;
    }

    /**
     *
     */
    public WorldObject(){
        this.name = "";
    }

    /**
     *
     * @return
     */
    public String getName(){ return name; }

    /**
     *
     * @return
     */
    public abstract WorldObject copy();
}
