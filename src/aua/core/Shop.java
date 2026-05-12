package aua.core;

import aua.core.exceptions.MalformedStringException;
import aua.utils.StringUtil;

public class Shop {
    private static final int DEFAULT_CAPACITY = 20;

    private Item[] products;
    private int size;

    /**
     *
     */
    public Shop() {
        this(DEFAULT_CAPACITY);
    }

    /**
     *
     * @param capacity
     */
    public Shop(int capacity) {
        if (capacity <= 0) {
            capacity = DEFAULT_CAPACITY;
        }
        this.products = new Item[capacity];
        this.size = 0;
    }

    /**
     *
     * @param reconstructableString
     */
    public Shop(String reconstructableString){
        this();

        String[] parsedString = StringUtil.parseDelimitedString(reconstructableString);

        if(parsedString[0].equals("SHOP")){
            for (int i = 1; i < parsedString.length; i++) {
                String[] parsedItemData = StringUtil.parseDelimitedString(parsedString[i], StringUtil.separator);
                if(parsedItemData.length>0 && parsedItemData[0].equals("PLANT")){
                    this.addProduct(new Plant(parsedString[i]));
                } else if (parsedItemData.length>0 && parsedItemData[0].equals("PRODUCT")){
                    this.addProduct(new Product(parsedString[i]));
                }
            }
        } else {
            throw new MalformedStringException();
        }
    }

    /**
     *
     * @param product
     * @return
     */
    public boolean addProduct(Item product) {
        if (product == null || isFull()) {
            return false;
        }
        products[size] = product;
        size++;
        return true;
    }

    /**
     *
     * @param player
     * @param item
     * @return
     */
    public boolean buy(Player player, Item item) {
        if (player == null || item == null) {
            return false;
        }
        if (!contains(item)) {
            return false;
        }
        if (player.isInventoryFull()) {
            return false;
        }
        if (!player.spendMoney(item.getBuyPrice())) {
            return false;
        }
        return player.addToInventory(item);
    }

    /**
     *
     * @param player
     * @param product
     * @return
     */
    public boolean sell(Player player, Product product) {
        if (player == null || product == null) {
            return false;
        }
        if (!player.removeFromInventory(product)) {
            return false;
        }
        player.addMoney(product.getSellPrice());
        return true;
    }

    /**
     *
     * @param product
     * @return
     */
    public boolean contains(Item product) {
        for (int i = 0; i < size; i++) {
            if (products[i] == product) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @return
     */
    public Item[] getProducts() {
        Item[] copy = new Item[size];
        for (int i = 0; i < size; i++) {
            copy[i] = products[i];
        }
        return copy;
    }

    /**
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @return
     */
    public boolean isFull() {
        return size == products.length;
    }

    /**
     *
     * @return
     */
    public String toString(){
        String shopString = "SHOP"+StringUtil.defaultDelimiter;

        for (int i = 0; i < size; i++) {
            if(this.products[i]!=null){
                if(i!=size-1){
                    shopString = shopString+products[i].toString()+StringUtil.defaultDelimiter;
                } else {
                    shopString = shopString+products[i].toString();
                }

            }
        }

        return shopString;
    }
}
