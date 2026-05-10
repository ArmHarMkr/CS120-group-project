package aua.core;

public interface Item {
    String getName();
    int getBuyPrice();
    WorldObject copy();
}