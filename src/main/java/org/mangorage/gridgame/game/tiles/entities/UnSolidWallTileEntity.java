package org.mangorage.gridgame.game.tiles.entities;

import net.querz.nbt.tag.CompoundTag;
import org.mangorage.gridgame.api.grid.Grid;
import org.mangorage.gridgame.api.grid.ITile;
import org.mangorage.gridgame.game.Game;
import org.mangorage.gridgame.registry.Tiles;

public class UnSolidWallTileEntity extends TileEntity {
    private int ticks = 0;
    private boolean solid = false;
    private boolean tick = false;

    public UnSolidWallTileEntity(int x, int y) {
        super(x, y);
    }

    @Override
    public void tick() {
        ticks++;
        if (ticks % 20 == 0) {
            solid = !solid;
        }
        if (ticks % 2 == 0 && tick) {
            Grid grid = Game.getInstance().getGrid();
            int sizeY = grid.getSizeY();
            if (getY() < sizeY) {
                int newY = getY() + 1;
                ITile tile = grid.getGridTile(getX(), newY).getTile();
                if (tile == Tiles.EMPTY_TILE) {
                    grid.setTile(getX(), getY(), Tiles.EMPTY_TILE);
                    grid.setTile(getX(), newY, Tiles.UN_SOLID_TILE);
                    var entity = grid.getGridTile(getX(), newY).getTileEntity(this.getClass());
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
