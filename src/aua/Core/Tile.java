package aua.Core;

public class Tile {
    private TerrainType type;
    private WorldObject object;

    public Tile(TerrainType type){
        this.type = type;
    }

    public boolean isWalkable(){

        if(object != null){
            return false;
        }

        return type == TerrainType.ROAD;
    }

    public boolean isPlaceable(){

        if(object != null){
            return false;
        }

        return type == TerrainType.SOIL;
    }

    public boolean place(WorldObject obj){
        if(!isPlaceable())
            return false;

        object = obj;
        return true;
    }

    public WorldObject remove(){
        WorldObject temp = object;
        object = null;
        return temp;
    }

    public void tick(){
        if(object instanceof Plant){
            ((Plant) object).grow();
        }
    }

    public WorldObject getObject(){
        return object;
    }

    public TerrainType getType(){
        return type;
    }
}
