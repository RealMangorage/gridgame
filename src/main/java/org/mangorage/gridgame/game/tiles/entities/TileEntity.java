package org.mangorage.gridgame.game.tiles.entities;


import org.mangorage.gridgame.api.grid.Grid;
import org.mangorage.gridgame.api.grid.ITileEntity;

public abstract class TileEntity implements ITileEntity {
    private final Grid grid;
    private final int x, y, z;

    public TileEntity(Grid grid, int x, int y, int z) {
        this.grid = grid;
        this.x = x;
        this.y = y;
        this.z =  z;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
