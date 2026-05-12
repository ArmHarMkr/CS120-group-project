package aua.core;

import aua.utils.StringUtil;
import aua.core.exceptions.MalformedStringException;

public class Player {

    private static final int DEFAULT_MONEY = 100;
    private Inventory inventory;
    private int money;

    /**
     *
     */
    public Player() {
        this.money = DEFAULT_MONEY;
        this.inventory = new Inventory();
    }

    /**
     *
     * @param money
     */
    public Player(int money) {
        if (money < 0) {
            money = DEFAULT_MONEY;}
        this.money = money;
        this.inventory = new Inventory();
    }

    /**
     *
     * @param playerReconstructableString
     * @param inventoryReconstructableString
     * @throws MalformedStringException
     * @throws NumberFormatException
     */
    public Player(String playerReconstructableString, String inventoryReconstructableString) throws MalformedStringException, NumberFormatException {
        String[] playerData = StringUtil.parseDelimitedString(playerReconstructableString);
        if(playerData[0].equals("PLAYER")){
            int money = Integer.parseInt(playerData[2]);
            this.addMoney(money);
        }else {
            throw new MalformedStringException();
        }

        String[] inventoryData = StringUtil.parseDelimitedString(inventoryReconstructableString);
        if(inventoryData[0].equals("INVENTORY")){
            Inventory reconstructedInventory = new Inventory();

            for(int i = 1; i < inventoryData.length; i++){
                String[] parsedInventoryItemData = StringUtil.parseDelimitedString(inventoryData[i],StringUtil.separator);
                if (parsedInventoryItemData[0].equals("PLANT")){
                    Plant reconstructedPlant = new Plant(inventoryData[i]);
                    reconstructedInventory.addItem(reconstructedPlant);
                } else if (parsedInventoryItemData[0].equals("PRODUCT")){
                    Product reconstructedProduct = new Product(inventoryData[i]);
                    reconstructedInventory.addItem(reconstructedProduct);
                }
            }

            this.inventory = reconstructedInventory;

        } else {
            throw new MalformedStringException();
        }

    }

    /**
     *
     * @param item
     * @return
     */
    public boolean addToInventory(Item item) {
        return inventory.addItem(item);
    }

    /**
     *
     * @param item
     * @return
     */
    public boolean removeFromInventory(Item item) {
        return inventory.removeItem(item);
    }

    /**
     *
     * @return
     */
    public boolean isInventoryFull() {
        return inventory.isFull();
    }

    /**
     *
     * @return
     */
    public int getMoney() {
        return money;
    }

    /**
     *
     * @return
     */
    public Item[] getInventoryItems() {
        return inventory.getItems();
    }

    /**
     *
     * @return
     */
    public int getSelectedInventoryIndex() {
        return inventory.getSelectedIndex();
    }

    /**
     *
     * @param index
     * @return
     */
    public boolean selectItem(int index) {
        return inventory.selectItem(index);
    }

    /**
     *
     * @return
     */
    public Item getSelectedItem() {
        return inventory.getSelectedItem();
    }

    /**
     *
     * @return
     */
    public Item takeSelectedItem() {
        return inventory.removeSelectedItem();
    }

    /**
     *
     * @param amount
     * @return
     */
    public boolean spendMoney(int amount) {
        if (amount <= 0 || money < amount) {
            return false;
        }
        money -= amount;
        return true;
    }

    /**
     *
     * @param amount
     */
    public void addMoney(int amount) {
        if (amount > 0) {
            money += amount;
        }
    }

    /**
     *
     * @param tile
     * @return
     */
    public Product harvest(Tile tile){
        return null;
    }
}
