package org.mangorage.gridgame.game.tiles;

import org.mangorage.gridgame.api.grid.IEntityTile;
import org.mangorage.gridgame.api.grid.ITile;
import org.mangorage.gridgame.game.Game;
import org.mangorage.gridgame.game.tiles.entities.ExampleTileEntity;


public class UnSolidTile implements ITile, IEntityTile<ExampleTileEntity> {

    @Override
    public boolean isSolid(int x, int y) {
        var gridTile = Game.getInstance().getGrid().getGridTile(x, y);
        var entity = gridTile.getTileEntity(ExampleTileEntity.class);
        if (entity != null) return entity.isSolid();
        return false;
    }

    @Override
    public ExampleTileEntity createTileEntity(int x, int y) {
        return new ExampleTileEntity(x, y);
    }
}
