package aua.ui;

import aua.core.Playable;
import aua.core.exceptions.GameActionException;

import javax.swing.*;

public class GrandmasGardenUI extends JFrame implements Playable {
    public GrandmasGardenUI(){
        setTitle("Grandamas Garden");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }

    public void start() {

    }

    public void draw() {

    }


    public void handlePlanting() throws GameActionException {

    }

    public void handleCollecting() throws GameActionException {

    }

    public void drawShop() {

    }
}
