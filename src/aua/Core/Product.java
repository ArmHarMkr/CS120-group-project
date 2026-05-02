package aua.Core;

public class Product extends WorldObject {

    private int sellPrice;
    private int buyPrice;

    public Product(String name, int buyPrice, int sellPrice){
        super(name);
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public Product(Product other){
        super(other.name);
        this.buyPrice = other.buyPrice;
        this.sellPrice = other.sellPrice;
    }

    public WorldObject copy(){
        return new Product(this);
    }

    public int getSellPrice(){
        return sellPrice;
    }
    public int getBuyPrice(){
        return buyPrice;
    }

}