package org.mangorage.gridgame.game.tiles;

import org.mangorage.gridgame.core.grid.Grid;
import org.mangorage.gridgame.core.grid.tiles.ITileEntityTicker;
import org.mangorage.gridgame.core.grid.tiles.IEntityTile;
import org.mangorage.gridgame.core.grid.tiles.Tile;
import org.mangorage.gridgame.core.grid.tiles.TileEntity;
import org.mangorage.gridgame.game.tiles.entities.UnSolidWallTileEntity;


public class UnSolidWallTile extends Tile implements IEntityTile {

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
    public <T extends TileEntity> ITileEntityTicker<T> getTicker() {
        return (grid, pos, tileEntity) -> ((UnSolidWallTileEntity) tileEntity).tick();
    }
}
