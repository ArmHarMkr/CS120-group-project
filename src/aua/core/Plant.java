package aua.core;

import aua.utils.StringUtil;
import aua.core.exceptions.MalformedStringException;

public class Plant extends WorldObject {

    private int growthPeriod;
    private int currentGrowth;
    private Product harvestProduct;

    /**
     *
     * @param name
     * @param growthPeriod
     */
    public Plant(String name, int growthPeriod){
        this(name, growthPeriod, new Product(name.replace(" Seed", ""), 0, 2));
    }

    /**
     *
     * @param name
     * @param growthPeriod
     * @param harvestProduct
     */
    public Plant(String name, int growthPeriod, Product harvestProduct){
        super(name);
        this.growthPeriod = growthPeriod;
        this.harvestProduct = harvestProduct;

    }

    /**
     *
     * @return
     */
    public int getBuyPrice(){
        return harvestProduct.getBuyPrice();
    }

    /**
     *
     * @param other
     */
    public Plant(Plant other){
        super(other.name);
        this.growthPeriod = other.growthPeriod;
        this.currentGrowth = other.currentGrowth;
        this.harvestProduct = new Product(other.harvestProduct);
    }

    /**
     *
     * @param reconstructableString
     * @throws MalformedStringException
     * @throws NumberFormatException
     */
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

    /**
     *
     * @return
     */
    public int getGrowthPeriod() {
        return growthPeriod;
    }

    /**
     *
     * @return
     */
    public int getCurrentGrowth() {
        return currentGrowth;
    }

    /**
     *
     * @return
     */
    public WorldObject copy(){
        return new Plant(this);
    }

    /**
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public Product getProductParameters() throws CloneNotSupportedException {
        return harvestProduct.clone();
    }

    /**
     *
     */
    public void grow(){
        currentGrowth++;
    }

    /**
     *
     * @return
     */
    public boolean isReady(){
        return currentGrowth >= growthPeriod;
    }

    /**
     *
     * @return
     */
    public Product collect(){
        if(!isReady()){
            return null;
        }

        return new Product(harvestProduct);
    }

    /**
     *
     * @return
     */
    public String toString(){
        return "PLANT"+StringUtil.separator+this.getName()+StringUtil.separator+this.getCurrentGrowth()+StringUtil.separator+this.getGrowthPeriod()+StringUtil.separator+this.harvestProduct.getName()+StringUtil.separator+this.harvestProduct.getBuyPrice()+StringUtil.separator+this.harvestProduct.getSellPrice();
    }
}
