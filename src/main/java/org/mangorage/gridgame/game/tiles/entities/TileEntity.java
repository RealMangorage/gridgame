package org.mangorage.gridgame.game.tiles.entities;


import org.mangorage.gridgame.api.grid.ITileEntity;

public abstract class TileEntity implements ITileEntity {
    private final int x, y;

    public TileEntity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
