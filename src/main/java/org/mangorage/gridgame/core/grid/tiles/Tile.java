package org.mangorage.gridgame.core.grid.tiles;

import org.mangorage.gridgame.core.grid.Grid;

public abstract class Tile {
    abstract public boolean isSolid(Grid grid, int x, int y);

    public boolean canSave() {
        return true;
    }
}
