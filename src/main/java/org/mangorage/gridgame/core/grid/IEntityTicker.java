package org.mangorage.gridgame.core.grid;

import org.mangorage.gridgame.game.tiles.entities.TileEntity;

public interface IEntityTicker<T extends TileEntity> {
    void tick(Grid grid, int x, int y, int z, T tileEntity);
}
