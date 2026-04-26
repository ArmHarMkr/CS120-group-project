package aua.Core;

public class Inventory {

    private static final int DEFAULT_CAPACITY = 20;
    private WorldObject[] items;
    private int size;
    private int selectedIndex;

    public Inventory() {
        this(DEFAULT_CAPACITY);
    }

    public Inventory(int capacity) {
        if (capacity <= 0) {capacity = DEFAULT_CAPACITY;}
        this.items = new WorldObject[capacity];
        this.size = 0;
        this.selectedIndex = -1;
    }

    public boolean addItem(WorldObject item) {
        if (item == null || isFull()) {return false;}
        items[size] = item;
        if (selectedIndex == -1) {selectedIndex = 0;}
        size++;
        return true;
    }

    public boolean selectItem(int index) {
        if (index < 0 || index >= size) {
            return false;
        }
        selectedIndex = index;
        return true;
    }

    public WorldObject getSelectedItem() {
        if (selectedIndex == -1) {
            return null;
        }
        return items[selectedIndex];
    }

    public WorldObject removeSelectedItem() {
        if (selectedIndex == -1) {
            return null;
        }
        WorldObject removedItem = items[selectedIndex];
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

    public boolean removeItem(WorldObject item) {
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

    public WorldObject[] getItems() {
        WorldObject[] copy = new WorldObject[size];
        for (int i = 0; i < size; i++) {
            copy[i] = items[i];
        }
        return copy;
    }
    public int getSize() {
        return size;
    }
    public int getCapacity() {
        return items.length;
    }
    public int getSelectedIndex() {
        return selectedIndex;
    }
    public boolean isFull() {
        return size == items.length;
    }
    public boolean isEmpty() {
        return size == 0;
    }
}