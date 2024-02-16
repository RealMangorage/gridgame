package org.mangorage.gridgame.api.grid;

public interface ITile {
    boolean isSolid(int x, int y);
    default boolean canSave() {
        return true;
    }
}
