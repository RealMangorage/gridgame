package org.mangorage.gridgame.game.tiles;

import org.mangorage.gridgame.api.grid.ITile;


public class EmptyTile implements ITile {
    @Override
    public boolean isSolid(int x, int y) {
        return false;
    }
}
