package org.mangorage.gridgame.core.grid;

public interface ITile {
    boolean isSolid(Grid grid, int x, int y);
    default boolean canSave() {
        return true;
    }
}
