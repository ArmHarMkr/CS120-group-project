package aua.ui;

import aua.core.GameManager;
import aua.core.Playable;
import aua.core.TerrainType;
import aua.core.exceptions.GameActionException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class GrandmasGardenUI extends JFrame implements Playable {
    private static final int TILE_SIZE = 40;
    private final int PLANT_GROWTH_STAGES = 10;

    private GameManager gameManager;
    private JPanel mapPanel;
    private JLabel messageLabel;
    private MapSquare[][] squares;
    private ImageIcon soilIcon;
    private ImageIcon roadIcon;
    private ImageIcon shopIcon;
    private ImageIcon rockIcon;
    private ImageIcon playerIcon;
    private ImageIcon[] plantIcons;
    private ImageIcon maturePlantIcon;
    private enum ActionMode {
        NONE,
        PLANTING,
        HARVESTING
    }
    private ActionMode actionMode = ActionMode.NONE;

    public GrandmasGardenUI(GameManager gameManager){
        this.gameManager = gameManager;
        this.plantIcons = new ImageIcon[PLANT_GROWTH_STAGES];
        loadImages();
        setTitle("Grandma's Garden");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mapPanel = new JPanel(new GridLayout(gameManager.getMapHeight(), gameManager.getMapWidth(), 0, 0));
        squares = new MapSquare[gameManager.getMapHeight()][gameManager.getMapWidth()];

        for(int i = 0; i < gameManager.getMapHeight(); i++){
            for(int j = 0; j < gameManager.getMapWidth(); j++){
                final int y = i;
                final int x = j;

                squares[i][j] = new MapSquare();
                squares[i][j].addActionListener(e -> handleTileClick(x, y));
                mapPanel.add(squares[i][j]);
            }
        }

        messageLabel = new JLabel(gameManager.getMessage());
        messageLabel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        add(mapPanel, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);
        bindMovementKeys();
        draw();
        pack();
        setLocationRelativeTo(null);
    }

    public void start() {
        setVisible(true);
        mapPanel.requestFocusInWindow();
    }

    public void save() {

    }

    public void load(){

    }

    public void draw() {
        for(int i = 0; i < gameManager.getMapHeight(); i++){
            for(int j = 0; j < gameManager.getMapWidth(); j++){
                squares[i][j].setSquareIcon(getIconFor(j, i));
            }
        }

        messageLabel.setText(gameManager.getMessage());
        repaint();
    }


    public void handlePlanting() throws GameActionException {

    }

    public void handleCollecting() throws GameActionException {

    }

    public void drawShop() {
        ShopWindow shopUI = new ShopWindow(this, gameManager);
        shopUI.open();
        draw();
    }

    private void bindMovementKeys(){
        bindMovementKey("W", 'w');
        bindMovementKey("A", 'a');
        bindMovementKey("S", 's');
        bindMovementKey("D", 'd');


        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("E"), "E");
        getRootPane().getActionMap().put("E", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                try {
                    gameManager.enterShop();
                    drawShop();
                } catch(GameActionException exception){
                    gameManager.setMessage(exception.getMessage());
                    draw();
                }
            }
        });
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("P"), "P");
        getRootPane().getActionMap().put("P", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                actionMode = ActionMode.PLANTING;
                gameManager.setMessage("Choose a highlighted soil tile to plant.");
                draw();
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("H"), "H");
        getRootPane().getActionMap().put("H", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                actionMode = ActionMode.HARVESTING;
                gameManager.setMessage("Choose a highlighted mature plant to harvest.");
                draw();
            }
        });
    }

    private void bindMovementKey(String key, char direction){
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), key);
        getRootPane().getActionMap().put(key, new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                try {
                    gameManager.handleMovement(direction);
                } catch(GameActionException exception){
                    gameManager.setMessage(exception.getMessage());
                }

                draw();
            }
        });
    }

    private ImageIcon getIconFor(int x, int y){
        if(x == gameManager.getPlayerX() && y == gameManager.getPlayerY()){
            return playerIcon;
        }

        if(gameManager.hasPlantAt(x, y)){
            if(gameManager.isMaturePlantAt(x, y)){
                return maturePlantIcon;
            } else {
                int growthRatio = gameManager.getGrowthRatioAt(x,y);

                return this.plantIcons[growthRatio];
            }
        }

        TerrainType type = gameManager.getTerrainTypeAt(x, y);
        if(type == TerrainType.SOIL){
            return soilIcon;
        }
        if(type == TerrainType.ROCK){
            return rockIcon;
        }
        if(type == TerrainType.SHOP){
            return shopIcon;
        }
        return roadIcon;
    }

    private void loadImages(){
        soilIcon = loadIcon("/aua/images/muddy_ground.jpg");
        roadIcon = loadIcon("/aua/images/Ground_Diffuse.jpg");
        rockIcon =loadIcon("/aua/images/LongLeaves_S.jpg");
        shopIcon= loadIcon("/aua/images/shop.png");
        playerIcon = loadIcon("/aua/images/farmera1.jpg");

        for(int i = 0; i<PLANT_GROWTH_STAGES; i++){
            String path = "/aua/images/wild_plant_grow_"+(i+1)+".png";
            plantIcons[i] = loadIcon(path);
        }

        maturePlantIcon = loadIcon("/aua/images/wild_plant_grow_12.png");
    }

    private ImageIcon loadIcon(String path){
        URL imageUrl = getClass().getResource(path);
        if(imageUrl == null){
            return createFallbackIcon(Color.GRAY);
        }

        Image image = new ImageIcon(imageUrl).getImage();
        Image scaledImage = image.getScaledInstance(TILE_SIZE, TILE_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private ImageIcon createFallbackIcon(Color color){
        BufferedImage image = new BufferedImage(
                TILE_SIZE,
                TILE_SIZE,
                BufferedImage.TYPE_INT_RGB
        );
        Graphics graphics = image.getGraphics();
        graphics.setColor(color);
        graphics.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        graphics.dispose();

        return new ImageIcon(image);
    }
    private void handleTileClick(int x, int y){
        if(actionMode == ActionMode.NONE){
            return;
        }

        char direction = getDirectionToTile(x, y);

        if(direction == ' '){
            gameManager.setMessage("Choose a tile next to the player.");
            draw();
            return;
        }

        try {
            if(actionMode == ActionMode.PLANTING){
                gameManager.plant(direction);
            } else if(actionMode == ActionMode.HARVESTING){
                gameManager.collect(direction);
            }

            actionMode = ActionMode.NONE;
        } catch(GameActionException exception){
            gameManager.setMessage(exception.getMessage());
        }

        draw();
    }

    private char getDirectionToTile(int x, int y){
        int dx = x - gameManager.getPlayerX();
        int dy = y - gameManager.getPlayerY();

        if(dx == -1 && dy == -1) return 'q';
        if(dx == 0 && dy == -1) return 'w';
        if(dx == 1 && dy == -1) return 'e';
        if(dx == -1 && dy == 0) return 'a';
        if(dx == 1 && dy == 0) return 'd';
        if(dx == -1 && dy == 1) return 'z';
        if(dx == 0 && dy == 1) return 's';
        if(dx == 1 && dy == 1) return 'c';

        return ' ';
    }

    private void updateHighlights(){
        for(int y = 0; y < gameManager.getMapHeight(); y++){
            for(int x = 0; x < gameManager.getMapWidth(); x++){
                squares[y][x].setHighlighted(false, Color.YELLOW);
            }
        }

        if(actionMode == ActionMode.NONE){
            return;
        }

        int playerX = gameManager.getPlayerX();
        int playerY = gameManager.getPlayerY();

        for(int dy = -1; dy <= 1; dy++){
            for(int dx = -1; dx <= 1; dx++){
                if(dx == 0 && dy == 0){
                    continue;
                }

                int x = playerX + dx;
                int y = playerY + dy;

                if(x < 0 || x >= gameManager.getMapWidth() || y < 0 || y >= gameManager.getMapHeight()){
                    continue;
                }

                if(actionMode == ActionMode.PLANTING){
                    if(gameManager.getTerrainTypeAt(x, y) == TerrainType.SOIL && !gameManager.hasPlantAt(x, y)){
                        squares[y][x].setHighlighted(true, Color.GREEN);
                    }
                }

                if(actionMode == ActionMode.HARVESTING){
                    if(gameManager.isMaturePlantAt(x, y)){
                        squares[y][x].setHighlighted(true, Color.ORANGE);
                    }
                }
            }
        }
    }
}
