package aua.Core;

public class GameMap {
    private Tile[][] tiles;

    public GameMap(int width, int height){
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
}
