package aua.Core;

public class Plant extends WorldObject {

    private int growthPeriod;
    private int currentGrowth;
    private Product harvestProduct;

    public Plant(String name, int growthPeriod, Product product){
        super(name);
        this.growthPeriod = growthPeriod;
        this.harvestProduct = product;
    }

    public void grow(){
        currentGrowth++;
    }

    public boolean isReady(){
        return currentGrowth >= growthPeriod;
    }

    public Product harvest(){
        if(!isReady()) return null;

        currentGrowth = 0;
        return harvestProduct;
    }
}