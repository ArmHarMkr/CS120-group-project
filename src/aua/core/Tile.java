package aua.core;

public class Tile {
    private TerrainType type;
    private WorldObject object;

    /**
     *
     * @param type
     */
    public Tile(TerrainType type){
        this.type = type;
    }

    /**
     *
     * @param type
     * @param object
     */
    public Tile(TerrainType type, WorldObject object){
        this.object = object;
        this.type = type;
    }

    /**
     *
     * @return
     */
    public boolean isWalkable(){

        if(object != null){
            return false;
        }

        return type == TerrainType.ROAD;
    }

    /**
     *
     * @return
     */
    public boolean isPlaceable(){

        if(object != null){
            return false;
        }

        return type == TerrainType.SOIL;
    }

    /**
     *
     * @param obj
     * @return
     */
    public boolean place(WorldObject obj){
        if(!isPlaceable())
            return false;

        object = obj;
        return true;
    }

    /**
     *
     * @return
     */
    public WorldObject remove(){
        WorldObject temp = object;
        object = null;
        return temp;
    }

    /**
     *
     * @return
     */
    public WorldObject getObject(){
        return object;
    }

    /**
     *
     * @return
     */
    public TerrainType getType(){
        return type;
    }

}
