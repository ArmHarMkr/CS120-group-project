package aua.Core;

public class GameMap {
    private Tile[][] tiles;
    private int width;
    private int height;

    public GameMap(int width, int height){
        this.width = width;
        this.height = height;
        tiles = new Tile[height][width];

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(x == 0 || y == 0 || x == width - 1 || y == height - 1){
                    tiles[y][x] = new Tile(TerrainType.ROCK);
                } else if(x % 4 == 1 || y % 4 == 1){
                    tiles[y][x] = new Tile(TerrainType.ROAD);
                } else if((x + y) % 8 == 0){
                    tiles[y][x] = new Tile(TerrainType.ROCK);
                } else {
                    tiles[y][x] = new Tile(TerrainType.SOIL);
                }
            }
        }
    }

    public Tile getTile(int x, int y){
        return tiles[y][x];
    }

    public boolean isInside(int x, int y){
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isWalkable(int x, int y){
        return isInside(x, y) && tiles[y][x].isWalkable();
    }

    public boolean placeObject(int x, int y, WorldObject object){
        return isInside(x, y) && tiles[y][x].place(object);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public String draw(int playerX, int playerY){
        String mapText = "";

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(x == playerX && y == playerY){
                    mapText += '@';
                } else if(tiles[y][x].getObject() instanceof Plant){
                    Plant plant = (Plant) tiles[y][x].getObject();
                    if(plant.isReady()){
                        mapText += 'M';
                    } else {
                        mapText += 'P';
                    }
                } else if(tiles[y][x].getType() == TerrainType.ROAD){
                    mapText += '.';
                } else if(tiles[y][x].getType() == TerrainType.SOIL){
                    mapText += ',';
                } else {
                    mapText += '#';
                }
            }
            mapText += '\n';
        }

        return mapText;
    }

    public void tickAll(){
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(tiles[y][x].getType() == TerrainType.SOIL){
                    tiles[y][x].tick();
                }
            }
        }
    }
}
