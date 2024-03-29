package org.mangorage.gridgame.game.tiles.entities;

import net.querz.nbt.tag.CompoundTag;
import org.mangorage.gridgame.core.grid.Grid;
import org.mangorage.gridgame.core.grid.tiles.Tile;
import org.mangorage.gridgame.core.grid.tiles.TileEntity;
import org.mangorage.gridgame.registry.Tiles;

public final class UnSolidWallTileEntity extends TileEntity {
    private int ticks = 0;
    private boolean solid = false;
    private static final boolean tick = false;

    public UnSolidWallTileEntity(Grid grid, int x, int y, int z) {
        super(grid, x, y, z);
    }

    public void tick() {
        ticks++;
        if (ticks % 20 == 0) {
            solid = !solid;
        }
        if (ticks % 2 == 0) {
            Grid grid = getGrid();
            int sizeY = grid.getSizeY();
            if (getY() < sizeY) {
                int newY = getY() + 1;
                Tile tile = grid.getGridTile(getX(), newY, getZ()).getTile();
                if (tile == Tiles.EMPTY_TILE.get()) {
                    grid.setTile(getX(), getY(), 0, Tiles.EMPTY_TILE.get());
                    grid.setTile(getX(), newY, 0, Tiles.UN_SOLID_TILE.get());
                    var entity = grid.getGridTile(getX(), newY, getZ()).getTileEntity(this.getClass());
                    if (entity != null) {
                        entity.setSolid(solid);
                        entity.setTicks(ticks);
                    }
                }
            }
        }
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putInt("ticks", ticks);
    }

    @Override
    public void load(CompoundTag tag) {
        this.ticks = tag.getInt("ticks");
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public boolean isSolid() {
        return solid;
    }
}
