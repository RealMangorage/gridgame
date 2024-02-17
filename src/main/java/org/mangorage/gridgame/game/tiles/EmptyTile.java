package org.mangorage.gridgame.game.tiles;

import org.mangorage.gridgame.api.grid.Grid;
import org.mangorage.gridgame.api.grid.ITile;


public class EmptyTile implements ITile {
    @Override
    public boolean isSolid(Grid grid, int x, int y) {
        return false;
    }

    @Override
    @Deprecated
    public boolean canSave() {
        return false;
    }
}
