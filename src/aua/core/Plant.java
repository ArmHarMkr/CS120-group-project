package aua.core;

public class Plant extends WorldObject {

    private int growthPeriod;
    private int currentGrowth;
    private Product harvestProduct;

    public Plant(String name, int growthPeriod){
        this(name, growthPeriod, new Product(name.replace(" Seed", ""), 0, 2));
    }

    public Plant(String name, int growthPeriod, Product harvestProduct){
        super(name);
        this.growthPeriod = growthPeriod;
        this.harvestProduct = harvestProduct;

    }
    public Plant(Plant other){
        super(other.name);
        this.growthPeriod = other.growthPeriod;
        this.currentGrowth = other.currentGrowth;
        this.harvestProduct = new Product(other.harvestProduct);
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

    public Product collect(){
        if(!isReady()){
            return null;
        }

        return new Product(harvestProduct);
    }


}
