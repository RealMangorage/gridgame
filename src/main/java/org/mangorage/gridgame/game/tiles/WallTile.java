package org.mangorage.gridgame.game.tiles;

import org.mangorage.gridgame.core.grid.Grid;
import org.mangorage.gridgame.core.grid.tiles.Tile;

public class WallTile extends Tile {
    @Override
    public boolean isSolid(Grid grid, int x, int y) {
        return true;
    }
}
