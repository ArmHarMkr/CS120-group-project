package aua.Core;

public class Product extends Item {

    private int sellPrice;
    private int buyPrice;

    public Product(String name, int buyPrice, int sellPrice){
        super(name);
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public int getSellPrice(){
        return sellPrice;
    }
    public int getBuyPrice(){
        return buyPrice;
    }
}