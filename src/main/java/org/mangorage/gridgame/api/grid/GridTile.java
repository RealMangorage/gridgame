package org.mangorage.gridgame.api.grid;

public final class GridTile {
    private final int x, y, z;
    private final ITile tile;
    private final ITileEntity entity;

    public GridTile(int x, int y, int z, ITile tile, ITileEntity tileEntity) {
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

    public ITileEntity getTileEntity() {
        return entity;
    }

    @SuppressWarnings("unchecked")
    public <T extends ITileEntity> T getTileEntity(Class<T> tileClass) {
        if (entity == null) return null;
        return (T) entity;
    }
}
