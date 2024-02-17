package org.mangorage.gridgame.game.tiles;

import org.mangorage.gridgame.api.grid.Grid;
import org.mangorage.gridgame.api.grid.IEntityTile;
import org.mangorage.gridgame.api.grid.ITile;
import org.mangorage.gridgame.game.tiles.entities.UnSolidWallTileEntity;


public class UnSolidWallTile implements ITile, IEntityTile<UnSolidWallTileEntity> {

    @Override
    public boolean isSolid(Grid grid, int x, int y) {
        var gridTile = grid.getGridTile(x, y, 0);
        var entity = gridTile.getTileEntity(UnSolidWallTileEntity.class);
        if (entity != null) return entity.isSolid();
        return false;
    }

    @Override
    public UnSolidWallTileEntity createTileEntity(Grid grid, int x, int y, int z) {
        return new UnSolidWallTileEntity(grid, x, y, z);
    }
}
