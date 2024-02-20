package org.mangorage.gridgame.core.grid;

import org.mangorage.gridgame.core.grid.tiles.ITileEntityTicker;
import org.mangorage.gridgame.core.grid.tiles.TileEntity;
import org.mangorage.gridgame.core.grid.tiles.TilePos;

import java.util.Objects;

public record BoundTickableTileEntity<T extends TileEntity>(ITileEntityTicker<T> ticker, Grid grid, TilePos pos, T tileEntity) {
    public void tick() {
        ticker.tick(grid, pos, tileEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundTickableTileEntity<?> that = (BoundTickableTileEntity<?>) o;
        return Objects.equals(grid, that.grid) && Objects.equals(pos, that.pos) && Objects.equals(tileEntity, that.tileEntity) && Objects.equals(ticker, that.ticker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }
}
