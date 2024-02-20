package org.mangorage.gridgame.core.grid.tiles;

import org.mangorage.gridgame.core.grid.Grid;

public interface ITileEntityTicker<T extends TileEntity> {
    void tick(Grid grid, TilePos pos, T tileEntity);
}
