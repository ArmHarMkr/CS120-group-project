package aua.Core;

import aua.Core.WorldObject;

public interface Item {
    String getName();
    WorldObject copy();
}