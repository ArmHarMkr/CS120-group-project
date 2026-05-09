package aua.core;

public class Product extends WorldObject implements Cloneable {

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

    public Product clone() throws CloneNotSupportedException{
        Product product = (Product) super.clone();

        product.buyPrice = this.buyPrice;
        product.sellPrice = this.sellPrice;
        product.name = this.name;

        return product;
    }

    public int getSellPrice(){
        return sellPrice;
    }
    public int getBuyPrice(){
        return buyPrice;
    }

}
