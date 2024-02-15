package org.mangorage.gridgame.game.tiles.entities;

import org.mangorage.gridgame.api.grid.Grid;
import org.mangorage.gridgame.api.grid.ITile;
import org.mangorage.gridgame.game.Game;
import org.mangorage.gridgame.registry.Tiles;

public class UnsolidWallTileEntity extends TileEntity {
    private int ticks = 0;
    private boolean solid = false;

    public UnsolidWallTileEntity(int x, int y) {
        super(x, y);
    }

    @Override
    public void tick() {
        ticks++;
        if (ticks % 20 == 0) {
            solid = !solid;
        }
        if (ticks % 25 == 0) {
            Grid grid = Game.getInstance().getGrid();
            int sizeY = grid.getSizeY();
            if (getY() < sizeY) {
                int newY = getY() + 1;
                ITile tile = grid.getGridTile(getX(), newY).getTile();
                if (tile == Tiles.EMPTY_TILE) {
                    grid.setTile(getX(), getY(), Tiles.EMPTY_TILE);
                    grid.setTile(getX(), newY, Tiles.UN_SOLID_TILE);
                    var entity = grid.getGridTile(getX(), newY).getTileEntity(this.getClass());
                    if (entity != null)
                        entity.setSolid(solid);
                }
            }
        }
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public boolean isSolid() {
        return solid;
    }
}
