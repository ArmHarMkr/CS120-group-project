package aua.Core;

public class Shop {

    private static final int DEFAULT_CAPACITY = 20;

    private Product[] products;
    private int size;
    public Shop() {
        this(DEFAULT_CAPACITY);
    }

    public Shop(int capacity) {
        if (capacity <= 0) {
            capacity = DEFAULT_CAPACITY;
        }
        this.products = new Product[capacity];
        this.size = 0;
    }

    public boolean addProduct(Product product) {
        if (product == null || isFull()) {
            return false;
        }
        products[size] = product;
        size++;
        return true;
    }

    public boolean buy(Player player, Product product) {
        if (player == null || product == null) {
            return false;
        }
        if (!contains(product)) {
            return false;
        }
        if (player.getInventory().isFull()) {
            return false;
        }
        if (!player.spendMoney(product.getBuyPrice())) {
            return false;
        }
        return player.getInventory().addItem(product);
    }

    public boolean sell(Player player, Product product) {
        if (player == null || product == null) {
            return false;
        }
        if (!player.getInventory().removeItem(product)) {
            return false;
        }
        player.addMoney(product.getSellPrice());
        return true;
    }

    public boolean contains(Product product) {
        for (int i = 0; i < size; i++) {
            if (products[i] == product) {
                return true;
            }
        }

        return false;
    }

    public Product[] getProducts() {
        Product[] copy = new Product[size];
        for (int i = 0; i < size; i++) {
            copy[i] = products[i];
        }
        return copy;
    }

    public int getSize() {
        return size;
    }

    public boolean isFull() {
        return size == products.length;
    }
}