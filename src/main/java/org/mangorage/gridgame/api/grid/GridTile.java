package org.mangorage.gridgame.api.grid;

public final class GridTile {
    private final int x, y;
    private ITile tile;
    private ITileEntity entity;

    public GridTile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setTile(ITile tile) {
        this.tile = tile;
        this.entity = null;
        if (tile instanceof IEntityTile<?> entityTile)
            this.entity = entityTile.createTileEntity(x, y);
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
