package aua.core;

import aua.utils.StringUtil;
import aua.core.exceptions.MalformedStringException;

public class GameMap {
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
    }

    public GameMap(String metadata, String[] reconstructableStrings) throws NumberFormatException {
        String[] parsedMetadata = StringUtil.parseDelimitedString(metadata);

        if(parsedMetadata[0].equals("GAMEMAP")){
            String[] mapSizes = StringUtil.parseDelimitedString(parsedMetadata[1], StringUtil.separator);
            this.width = Integer.parseInt(mapSizes[0]);
            this.height = Integer.parseInt(mapSizes[1]);
        } else {
            throw new MalformedStringException();
        }

        this.tiles = new Tile[this.height][this.width];

        for (int i = 0; i < reconstructableStrings.length; i++) {
            String[] parsedReconstrucatbleString = StringUtil.parseDelimitedString(reconstructableStrings[i]);

            if (parsedReconstrucatbleString[0].equals("TILE")){
                for (int j = 1; j < parsedReconstrucatbleString.length; j++) {
                    String[] parsedTileData = StringUtil.parseDelimitedString(parsedReconstrucatbleString[j], StringUtil.separator);
                    String parsedTileType = parsedTileData[0];

                    int tileY = Integer.parseInt(parsedTileData[1]);
                    int tileX = Integer.parseInt(parsedTileData[2]);


                    if(parsedTileType.equals("ROCK")){
                        this.tiles[tileY][tileX] = new Tile(TerrainType.ROCK);
                    } else if (parsedTileType.equals("ROAD")){
                        this.tiles[tileY][tileX] = new Tile(TerrainType.ROAD);
                    } else if(parsedTileType.equals("SOIL")){
                        if(parsedTileData.length>3 && parsedTileData[3].equals("PLANT")){
                            String plantString = parsedTileData[3]+StringUtil.separator+parsedTileData[4]+StringUtil.separator+parsedTileData[5]+StringUtil.separator+parsedTileData[6]+StringUtil.separator+parsedTileData[7]+StringUtil.separator+parsedTileData[8]+StringUtil.separator+parsedTileData[9];
                            Plant plant = new Plant(plantString);
                            this.tiles[tileY][tileX] = new Tile(TerrainType.SOIL, plant);
                        } else {
                            this.tiles[tileY][tileX] = new Tile(TerrainType.SOIL);
                        }
                    }
                }
            } else {
                throw new MalformedStringException();
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
                } else if(tiles[i][j].getObject() instanceof Plant){
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

    public String getMapMetadata(){
        return "GAMEMAP"+StringUtil.defaultDelimiter+this.width+StringUtil.separator+this.height;
    }

    public String[] getMapEncoding(){
        String[] mapEncoding = new String[this.height];

        for(int y = 0; y < mapEncoding.length; y++){
            mapEncoding[y] = "TILE";
            for(int x = 0; x < width; x++){
                WorldObject tileObject = this.tiles[y][x].getObject();
                if(tileObject!=null){
                    mapEncoding[y] = mapEncoding[y]+StringUtil.defaultDelimiter+this.tiles[y][x].getType()+StringUtil.separator+y+StringUtil.separator+x+StringUtil.separator+tileObject;
                } else {
                    mapEncoding[y] = mapEncoding[y]+StringUtil.defaultDelimiter+this.tiles[y][x].getType()+StringUtil.separator+y+StringUtil.separator+x+StringUtil.separator+"EMPTY";
                }
            }
        }

        return mapEncoding;
    }
}
