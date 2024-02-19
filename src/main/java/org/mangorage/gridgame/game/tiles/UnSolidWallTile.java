package org.mangorage.gridgame.game.tiles;

import org.mangorage.gridgame.api.grid.Grid;
import org.mangorage.gridgame.api.grid.IEntityTicker;
import org.mangorage.gridgame.api.grid.IEntityTile;
import org.mangorage.gridgame.api.grid.ITile;
import org.mangorage.gridgame.game.tiles.entities.TileEntity;
import org.mangorage.gridgame.game.tiles.entities.UnSolidWallTileEntity;


public class UnSolidWallTile implements ITile, IEntityTile {

    @Override
    public boolean isSolid(Grid grid, int x, int y) {
        var gridTile = grid.getGridTile(x, y, 0);
        var entity = gridTile.getTileEntity(UnSolidWallTileEntity.class);
        if (entity != null) return entity.isSolid();
        return false;
    }


    @Override
    public TileEntity createTileEntity(Grid grid, int x, int y, int z) {
        return new UnSolidWallTileEntity(grid, x, y, z);
    }

    @Override
    public <T extends TileEntity> IEntityTicker<T> getTicker() {
        return (grid, x, y, z, tileEntity) -> ((UnSolidWallTileEntity) tileEntity).tick();
    }
}
