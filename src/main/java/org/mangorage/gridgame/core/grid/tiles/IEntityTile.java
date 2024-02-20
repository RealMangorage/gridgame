package org.mangorage.gridgame.core.grid.tiles;

import org.mangorage.gridgame.core.grid.Grid;

public interface IEntityTile {
    TileEntity createTileEntity(Grid grid, int x, int y, int z);

    default <T extends TileEntity> ITileEntityTicker<T> getTicker() {
        return null;
    }
}
