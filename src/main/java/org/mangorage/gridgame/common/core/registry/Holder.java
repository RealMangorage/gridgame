package org.mangorage.gridgame.common.core.registry;

public interface Holder<T> {
    T get();

    String getID();
}