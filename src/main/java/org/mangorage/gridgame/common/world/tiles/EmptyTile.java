package org.mangorage.gridgame.common.world.tiles;

import org.mangorage.gridgame.common.world.Tile;

public class EmptyTile extends Tile {
    @Override
    public boolean hasRenderer() {
        return false;
    }
}
