package aua.Core;

public class Player {

    private static final int DEFAULT_MONEY = 100;
    private Inventory inventory;
    private int money;

    public Player() {
        this.money = DEFAULT_MONEY;
        this.inventory = new Inventory();
    }

    public Player(int money) {
        if (money < 0) {
            money = DEFAULT_MONEY;}
        this.money = money;
        this.inventory = new Inventory();
    }

    public boolean addToInventory(WorldObject item) {
        return inventory.addItem(item);
    }

    public boolean removeFromInventory(WorldObject item) {
        return inventory.removeItem(item);
    }

    public boolean isInventoryFull() {
        return inventory.isFull();
    }

    public int getMoney() {
        return money;
    }

    public boolean selectItem(int index) {
        return inventory.selectItem(index);
    }

    public WorldObject getSelectedItem() {
        return inventory.getSelectedItem();
    }

    public WorldObject takeSelectedItem() {
        return inventory.removeSelectedItem();
    }

    public boolean spendMoney(int amount) {
        if (amount <= 0 || money < amount) {
            return false;
        }
        money -= amount;
        return true;
    }

    public void addMoney(int amount) {
        if (amount > 0) {
            money += amount;
        }
    }

    public Product harvest(Tile tile){
        return null;
    }
}