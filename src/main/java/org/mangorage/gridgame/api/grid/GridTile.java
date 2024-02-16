package org.mangorage.gridgame.api.grid;

public final class GridTile {
    private final int x, y;
    private final ITile tile;
    private final ITileEntity entity;

    public GridTile(int x, int y, ITile tile, ITileEntity tileEntity) {
        this.x = x;
        this.y = y;
        this.tile = tile;
        this.entity = tileEntity;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

    @Deprecated
    public ITile get() {
        return tile;
    }
}
