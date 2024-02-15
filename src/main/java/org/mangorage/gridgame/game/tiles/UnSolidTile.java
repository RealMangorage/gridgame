package org.mangorage.gridgame.game.tiles;

import org.mangorage.gridgame.api.grid.IEntityTile;
import org.mangorage.gridgame.api.grid.ITile;
import org.mangorage.gridgame.game.Game;
import org.mangorage.gridgame.game.tiles.entities.UnsolidWallTileEntity;


public class UnSolidTile implements ITile, IEntityTile<UnsolidWallTileEntity> {

    @Override
    public boolean isSolid(int x, int y) {
        var gridTile = Game.getInstance().getGrid().getGridTile(x, y);
        var entity = gridTile.getTileEntity(UnsolidWallTileEntity.class);
        if (entity != null) return entity.isSolid();
        return false;
    }

    @Override
    public UnsolidWallTileEntity createTileEntity(int x, int y) {
        return new UnsolidWallTileEntity(x, y);
    }
}
