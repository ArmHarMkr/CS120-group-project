package aua.core;

public interface Playable {
    void start();
    void draw();
    void handlePlanting() throws Exception;
    void handleCollecting() throws Exception;
    void drawShop();

}
