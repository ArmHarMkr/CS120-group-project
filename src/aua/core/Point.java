package aua.core;

public class Point {
    private int x;
    private int y;

    /**
     *
     */
    public Point(){
        this.x = 0;
        this.y = 0;
    }

    /**
     *
     * @param x
     * @param y
     */
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param nextX
     */
    public void setX(int nextX){
        this.x = nextX;
    }

    /**
     *
     * @param nextY
     */
    public void setY(int nextY){
        this.y = nextY;
    }
}
