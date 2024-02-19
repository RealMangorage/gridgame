package org.mangorage.gridgame.game.tiles;

import org.mangorage.gridgame.core.grid.Grid;
import org.mangorage.gridgame.core.grid.ITile;

public class WallTile implements ITile {
    @Override
    public boolean isSolid(Grid grid, int x, int y) {
        return true;
    }
}
