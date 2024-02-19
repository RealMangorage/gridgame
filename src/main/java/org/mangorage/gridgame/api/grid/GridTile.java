package org.mangorage.gridgame.api.grid;

import org.mangorage.gridgame.game.tiles.entities.TileEntity;

public final class GridTile {
    private final int x, y, z;
    private final ITile tile;
    private final TileEntity entity;

    public GridTile(int x, int y, int z, ITile tile, TileEntity tileEntity) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tile = tile;
        this.entity = tileEntity;
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

    public ITile getTile() {
        return tile;
    }

    public TileEntity getTileEntity() {
        return entity;
    }

    @SuppressWarnings("unchecked")
    public <T extends TileEntity> T getTileEntity(Class<T> tileClass) {
        if (entity == null) return null;
        return (T) entity;
    }
}
