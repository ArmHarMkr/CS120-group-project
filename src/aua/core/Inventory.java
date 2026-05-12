package aua.core;

public class Inventory {

    private static final int DEFAULT_CAPACITY = 20;
    private Item[] items;
    private int size;
    private int selectedIndex;

    public Inventory() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * @param capacity
     */
    public Inventory(int capacity) {
        if (capacity <= 0) {capacity = DEFAULT_CAPACITY;}
        this.items = new Item[capacity];
        this.size = 0;
        this.selectedIndex = -1;
    }

    /**
     * @param item
     * @return
     */
    public boolean addItem(Item item) {
        if (item == null || isFull()) {return false;}
        items[size] = item;
        if (selectedIndex == -1) {selectedIndex = 0;}
        size++;
        return true;
    }

    /**
     *
     * @param index
     * @return
     */
    public boolean selectItem(int index) {
        if (index < 0 || index >= size) {
            return false;
        }
        selectedIndex = index;
        return true;
    }

    /**
     *
     * @return
     */
    public Item getSelectedItem() {
        if (selectedIndex == -1) {
            return null;
        }
        return items[selectedIndex];
    }

    /**
     *
     * @return
     */
    public Item removeSelectedItem() {
        if (selectedIndex == -1) {
            return null;
        }
        Item removedItem = items[selectedIndex];
        for (int i = selectedIndex; i < size - 1; i++) {
            items[i] = items[i + 1];
        }
        size--;
        items[size] = null;
        if (size == 0) {
            selectedIndex = -1;
        } else if (selectedIndex >= size) {
            selectedIndex = size - 1;
        }
        return removedItem;
    }

    /**
     *
     * @param item
     * @return
     */
    public boolean removeItem(Item item) {
        if (item == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (items[i] == item) {
                for (int j = i; j < size - 1; j++) {
                    items[j] = items[j + 1];
                }
                size--;
                items[size] = null;
                if (size == 0) {
                    selectedIndex = -1;
                } else if (selectedIndex >= size) {
                    selectedIndex = size - 1;
                }
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    public Item[] getItems() {
        WorldObject[] copy = new WorldObject[size];
        for (int i = 0; i < size; i++) {
            copy[i] = items[i].copy();
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
    public int getCapacity() {
        return items.length;
    }

    /**
     *
     * @return
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     *
     * @return
     */
    public boolean isFull() {
        return size == items.length;
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }
}
