package org.mangorage.gridgame.api.grid;

public interface IEntityTile<T extends ITileEntity> {
    T createTileEntity(Grid grid, int x, int y, int z);
}
