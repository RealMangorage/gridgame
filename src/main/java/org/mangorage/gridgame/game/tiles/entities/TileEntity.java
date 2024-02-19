package org.mangorage.gridgame.game.tiles.entities;


import net.querz.nbt.tag.CompoundTag;
import org.mangorage.gridgame.core.grid.Grid;

import java.util.Objects;

public abstract class TileEntity {
   private final Grid grid;
    private final int x, y, z;

    public TileEntity(Grid grid, int x, int y, int z) {
        this.grid = grid;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    abstract public void save(CompoundTag tag);
    abstract public void load(CompoundTag tag);

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TileEntity that = (TileEntity) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
