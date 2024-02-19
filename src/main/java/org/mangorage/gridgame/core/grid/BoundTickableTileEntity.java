package org.mangorage.gridgame.core.grid;

import org.mangorage.gridgame.game.tiles.entities.TileEntity;

import java.util.Objects;

public record BoundTickableTileEntity<T extends TileEntity>(IEntityTicker<T> ticker, Grid grid, int x, int y, int z, T tileEntity) {
    public void tick() {
        ticker.tick(grid, x, y, z, tileEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundTickableTileEntity<?> that = (BoundTickableTileEntity<?>) o;
        return x == that.x && y == that.y && z == that.z && Objects.equals(grid, that.grid) && Objects.equals(tileEntity, that.tileEntity) && Objects.equals(ticker, that.ticker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, grid, x, y, z, tileEntity);
    }
}
