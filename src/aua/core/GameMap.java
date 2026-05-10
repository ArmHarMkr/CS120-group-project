package aua.core;

public class GameMap {
    private final int SHOPX = 1;
    private Tile[][] tiles;
    private int width;
    private int height;

    public GameMap(int width, int height){
        this.width = width;
        this.height = height;
        tiles = new Tile[height][width];

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if(j == 0 || i == 0 || j == width - 1 || i == height - 1){
                    tiles[i][j] = new Tile(TerrainType.ROCK);
                } else if(j % 4 == 1 || i % 4 == 1){
                    tiles[i][j] = new Tile(TerrainType.ROAD);
                } else if((j + i) % 8 == 0){
                    tiles[i][j] = new Tile(TerrainType.ROCK);
                } else {
                    tiles[i][j] = new Tile(TerrainType.SOIL);
                }
            }
        }
        tiles[SHOPX][3] = new Tile(TerrainType.SHOP);
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

    public boolean isShop(int x, int y){
        return isInside(x, y) && tiles[y][x].getType() == TerrainType.SHOP;
    }

    public boolean placeObject(int x, int y, WorldObject object){
        return isInside(x, y) && tiles[y][x].place(object);
    }

    public Product collectPlant(int x, int y){
        if(!isInside(x, y)){
            return null;
        }

        WorldObject object = tiles[y][x].getObject();
        if(!(object instanceof Plant)){
            return null;
        }

        Plant plant = (Plant) object;
        Product product = plant.collect();
        if(product == null){
            return null;
        }

        tiles[y][x].remove();
        return product;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public String draw(int playerX, int playerY){
        String mapText = "";

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if(j == playerX && i == playerY){
                    mapText += '@';
                } else if(tiles[i][j].getType() == TerrainType.SHOP){
                    mapText += 'S';
                }
                else if(tiles[i][j].getObject() instanceof Plant){
                    Plant plant = (Plant) tiles[i][j].getObject();
                    if(plant.isReady()){
                        mapText += 'M';
                    } else {
                        mapText += 'P';
                    }
                } else if(tiles[i][j].getType() == TerrainType.ROAD){
                    mapText += '.';
                } else if(tiles[i][j].getType() == TerrainType.SOIL){
                    mapText += ',';
                } else {
                    mapText += '#';
                }
            }
            mapText += '\n';
        }

        return mapText;
    }

    public String[] getMapEncoding(String delimiter){
        String[] mapEncoding = new String[this.height];
        String separator = "-";
        for(int y = 0; y < height; y++){
            mapEncoding[y] = "TILE";
            for(int x = 0; x < width; x++){
                mapEncoding[y] = mapEncoding[y]+delimiter+this.tiles[y][x].getType()+separator+y+separator+x;
            }
        }

        return mapEncoding;
    }
}
