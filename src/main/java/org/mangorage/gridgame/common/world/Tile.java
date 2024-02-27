package org.mangorage.gridgame.common.world;

import org.mangorage.mangonetwork.core.Side;

public abstract class Tile {
    public TileEntity createTileEntity(Level level, TilePos tilePos, Side side) {
        return null;
    }
}
