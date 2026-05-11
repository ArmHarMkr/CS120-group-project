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

    private GameManager gameManager;
    private JPanel mapPanel;
    private JLabel messageLabel;
    private MapSquare[][] squares;
    private ImageIcon soilIcon;
    private ImageIcon rockIcon;
    private ImageIcon playerIcon;
    private ImageIcon plantIcon;
    private ImageIcon maturePlantIcon;

    public GrandmasGardenUI(GameManager gameManager){
        this.gameManager = gameManager;
        loadImages();
        setTitle("Grandma's Garden");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mapPanel = new JPanel(new GridLayout(gameManager.getMapHeight(), gameManager.getMapWidth(), 0, 0));
        squares = new MapSquare[gameManager.getMapHeight()][gameManager.getMapWidth()];

        for(int i = 0; i < gameManager.getMapHeight(); i++){
            for(int j = 0; j < gameManager.getMapWidth(); j++){
                squares[i][j] = new MapSquare();
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

    }

    private void bindMovementKeys(){
        bindMovementKey("W", 'w');
        bindMovementKey("A", 'a');
        bindMovementKey("S", 's');
        bindMovementKey("D", 'd');
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
            }

            return plantIcon;
        }

        TerrainType type = gameManager.getTerrainTypeAt(x, y);
        if(type == TerrainType.SOIL){
            return soilIcon;
        }

        return rockIcon;
    }

    private void loadImages(){
        soilIcon = loadIcon("/aua/images/muddy_ground.jpg");
        rockIcon = loadIcon("/aua/images/Ground_Diffuse.jpg");
        playerIcon = loadIcon("/aua/images/farmera1.png");
        plantIcon = loadIcon("/aua/images/wild_plant_grow_4.png");
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
}
