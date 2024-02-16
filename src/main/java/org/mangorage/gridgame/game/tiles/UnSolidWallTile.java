package org.mangorage.gridgame.game.tiles;

import org.mangorage.gridgame.api.grid.IEntityTile;
import org.mangorage.gridgame.api.grid.ITile;
import org.mangorage.gridgame.game.Game;
import org.mangorage.gridgame.game.tiles.entities.UnSolidWallTileEntity;


public class UnSolidWallTile implements ITile, IEntityTile<UnSolidWallTileEntity> {

    @Override
    public boolean isSolid(int x, int y) {
        var gridTile = Game.getInstance().getGrid().getGridTile(x, y);
        var entity = gridTile.getTileEntity(UnSolidWallTileEntity.class);
        if (entity != null) return entity.isSolid();
        return false;
    }

    @Override
    public UnSolidWallTileEntity createTileEntity(int x, int y) {
        return new UnSolidWallTileEntity(x, y);
    }
}
