package aua.ui;

import javax.swing.*;
import java.awt.*;

public class MapSquare extends JButton {
    private static final int SIZE = 40;

    public MapSquare(){
        setPreferredSize(new Dimension(SIZE, SIZE));
        setMinimumSize(new Dimension(SIZE, SIZE));
        setMaximumSize(new Dimension(SIZE, SIZE));
        setOpaque(true);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setMargin(new Insets(0, 0, 0, 0));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    public void setHighlighted(boolean highlighted, Color color){
        setBorderPainted(highlighted);

        if(highlighted){
            setBorder(BorderFactory.createLineBorder(color, 4));
        } else {
            setBorder(null);
        }
    }

    public void setSquareIcon(ImageIcon icon){
        setIcon(icon);
    }
}
