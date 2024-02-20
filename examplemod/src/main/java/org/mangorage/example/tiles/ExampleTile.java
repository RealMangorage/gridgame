package org.mangorage.example.tiles;

import org.mangorage.gridgame.core.grid.Grid;
import org.mangorage.gridgame.core.grid.tiles.Tile;

public class ExampleTile extends Tile {
    @Override
    public boolean isSolid(Grid grid, int x, int y) {
        return true;
    }
}
