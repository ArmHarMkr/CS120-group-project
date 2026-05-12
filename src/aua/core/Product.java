package aua.core;

import aua.utils.StringUtil;
import aua.core.exceptions.MalformedStringException;

public class Product extends WorldObject implements Cloneable {
    private int sellPrice;
    private int buyPrice;

    /**
     *
     * @param name
     * @param buyPrice
     * @param sellPrice
     */
    public Product(String name, int buyPrice, int sellPrice){
        super(name);
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    /**
     *
     * @param other
     */
    public Product(Product other){
        super(other.name);
        this.buyPrice = other.buyPrice;
        this.sellPrice = other.sellPrice;
    }

    /**
     *
     * @param reconstructableString
     * @throws NumberFormatException
     */
    public Product(String reconstructableString) throws NumberFormatException {
        String[] parsedData = StringUtil.parseDelimitedString(reconstructableString, StringUtil.separator);
        if(parsedData[0].equals("PRODUCT")){
            this.name = parsedData[1];
            this.buyPrice = Integer.parseInt(parsedData[2]);
            this.sellPrice = Integer.parseInt(parsedData[3]);
        } else {
            throw new MalformedStringException();
        }
    }

    /**
     *
     * @return
     */
    public WorldObject copy(){
        return new Product(this);
    }

    /**
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public Product clone() throws CloneNotSupportedException{
        Product product = (Product) super.clone();

        product.buyPrice = this.buyPrice;
        product.sellPrice = this.sellPrice;
        product.name = this.name;

        return product;
    }

    /**
     *
     * @return
     */
    public int getSellPrice(){
        return sellPrice;
    }

    /**
     *
     * @return
     */
    public int getBuyPrice(){
        return buyPrice;
    }

    /**
     *
     * @return
     */
    public String toString(){
        return "PRODUCT"+StringUtil.separator+this.getName()+StringUtil.separator+this.getBuyPrice()+StringUtil.separator+this.getSellPrice();
    }
}
