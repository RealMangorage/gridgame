package org.mangorage.gridgame.api.grid;

import org.mangorage.gridgame.game.tiles.entities.TileEntity;

public record GridTile(int x, int y, int z, ITile tile, TileEntity tileEntity) {

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
        return tileEntity;
    }

    @SuppressWarnings("unchecked")
    public <T extends TileEntity> T getTileEntity(Class<T> tileClass) {
        if (getTileEntity() == null) return null;
        return (T) getTileEntity();
    }
}
