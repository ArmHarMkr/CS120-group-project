package aua.core;

import aua.Utils.StringUtil;
import aua.core.exceptions.MalformedStringException;

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
    public int getBuyPrice(){
        return harvestProduct.getBuyPrice();
    }

    public Plant(Plant other){
        super(other.name);
        this.growthPeriod = other.growthPeriod;
        this.currentGrowth = other.currentGrowth;
        this.harvestProduct = new Product(other.harvestProduct);
    }

    public Plant(String reconstructableString) throws MalformedStringException, NumberFormatException {
        String[] plantDataStrings = StringUtil.parseDelimitedString(reconstructableString, StringUtil.separator);

        if(plantDataStrings[0].equals("PLANT")){
            this.name = plantDataStrings[1];
            this.growthPeriod = Integer.parseInt(plantDataStrings[3]);
            this.currentGrowth = Integer.parseInt(plantDataStrings[2]);

            String separator = StringUtil.separator;
            String productString = "PRODUCT"+separator+plantDataStrings[4]+separator+plantDataStrings[5]+separator+plantDataStrings[6];
            this.harvestProduct = new Product(productString);

        } else {
            throw new MalformedStringException();
        }

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

    public Product getProductParameters() throws CloneNotSupportedException {
        return harvestProduct.clone();
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
