package org.mangorage.gridgame.core.grid.tiles;

public record GridTile(TilePos tilePos, Tile tile, TileEntity tileEntity) {

    public int getX() {
        return tilePos.x();
    }

    public int getY() {
        return tilePos.y();
    }

    public int getZ() {
        return tilePos.z();
    }

    public Tile getTile() {
        return tile;
    }

    public TileEntity getTileEntity() {
        return tileEntity;
    }

    @SuppressWarnings("unchecked")
    public <T extends TileEntity> T getTileEntity(Class<T> tileClass) {
        if (getTileEntity() == null) return null;
        return (T) getTileEntity();
    }
}
