package org.mangorage.gridgame.api.grid;

import org.mangorage.gridgame.game.tiles.entities.TileEntity;

public interface ITile {
    boolean isSolid(Grid grid, int x, int y);
    default boolean canSave() {
        return true;
    }

    default IEntityTile getEntityTile() {
        return null;
    }
}
