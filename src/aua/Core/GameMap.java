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
                if(x % 3 == 1 || y % 3 == 1){
                    tiles[y][x] = new Tile(TerrainType.ROAD);
                } else {
                    tiles[y][x] = new Tile(TerrainType.ROCK);
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
}
