package aua.Core;

public class Plant extends WorldObject {

    private int growthPeriod;
    private int currentGrowth;

    public Plant(String name, int growthPeriod){
        super(name);
        this.growthPeriod = growthPeriod;

    }
    public Plant(Plant other){
        super(other.name);
        this.growthPeriod = other.growthPeriod;
        this.currentGrowth = other.currentGrowth;
    }

    public int getGrowthPeriod() {
        return growthPeriod;
    }

    public int getCurrentGrowth() {
        return currentGrowth;
    }

    public WorldObject copy(){
        return new Plant(this);
    }


    public void grow(){
        currentGrowth++;
    }

    public boolean isReady(){
        return currentGrowth >= growthPeriod;
    }



}