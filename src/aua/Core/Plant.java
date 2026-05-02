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


    public Plant(Plant other){
        super(other.name);
        this.growthPeriod = other.growthPeriod;
        this.currentGrowth = other.currentGrowth;
        this.harvestProduct = new Product(other.harvestProduct); // deep copy!
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

    public Product harvest(){
        if(!isReady()) return null;

        currentGrowth = 0;
        return harvestProduct;
    }


}