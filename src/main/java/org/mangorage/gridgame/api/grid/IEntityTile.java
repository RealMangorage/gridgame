package org.mangorage.gridgame.api.grid;

public interface IEntityTile<T extends ITileEntity> {
    T createTileEntity(int x, int y);
}
