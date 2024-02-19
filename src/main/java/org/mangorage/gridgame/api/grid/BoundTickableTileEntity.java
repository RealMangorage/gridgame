package org.mangorage.gridgame.api.grid;

import org.mangorage.gridgame.game.tiles.entities.TileEntity;

public record BoundTickableTileEntity<T extends TileEntity>(IEntityTicker<T> ticker, Grid grid, int x, int y, int z, T tileEntity) {
    public void tick() {
        ticker.tick(grid, x, y, z, tileEntity);
    }
}
