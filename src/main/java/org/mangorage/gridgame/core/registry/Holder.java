package org.mangorage.gridgame.core.registry;

public interface Holder<T> {
    T get();

    String getID();
}
