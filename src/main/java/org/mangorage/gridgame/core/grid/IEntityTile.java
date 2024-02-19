package org.mangorage.gridgame.core.grid;

import org.mangorage.gridgame.game.tiles.entities.TileEntity;

public interface IEntityTile {
    TileEntity createTileEntity(Grid grid, int x, int y, int z);

    @SuppressWarnings("all")
    default <T extends TileEntity> IEntityTicker<T> getTicker() {
        return null;
    }
}
