package org.mangorage.gridgame.api.grid;

import org.mangorage.gridgame.render.RenderManager;
import org.mangorage.gridgame.game.Game;
import org.mangorage.gridgame.registry.Tiles;

import java.awt.*;
import java.lang.reflect.Array;

public class Grid {
    private final GridTile[][] tiles;
    private final int sizeX, sizeY;

    private int boundsX, boundsY;
    private Grid nextLayer;

    public Grid(int x, int y, int boundsX, int boundsY, boolean border) {
        this.sizeX = x;
        this.sizeY = y;
        this.boundsX = boundsX;
        this.boundsY = boundsY;
        this.tiles = (GridTile[][]) Array.newInstance(GridTile.class, x, y);
        populate(border);
    }

    public Grid(int x, int y, int bX, int bY) {
        this(x, y, bX, bY, true);
    }

    public void tick() {
        for (int x = 0; x < this.sizeX; x++) {
            for (int y = 0; y < this.sizeY; y++) {
                var tileEntity = getGridTile(x, y).getTileEntity();
                if (tileEntity != null)
                    tileEntity.tick();
            }
        }

        if (getNextLayer() != null) getNextLayer().tick();
    }

    public Grid createNewLayer() {
        if (this.nextLayer != null) return nextLayer;
        this.nextLayer = new Grid(sizeX, sizeY, boundsX, boundsY, false);
        return nextLayer;
    }

    public void updateBounds(int scale) {
        //this.boundsX = Math.min(sizeX, scale ^ 2);
        //this.boundsY = Math.min(sizeY, scale ^ 2);
        if (getNextLayer() != null) getNextLayer().updateBounds(scale);
    }

    public Grid getNextLayer() {
        return nextLayer;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public GridTile getGridTile(int x, int y) {
        if (tiles[x][y] == null) tiles[x][y] = new GridTile(x, y);
        return tiles[x][y];
    }

    public ITile getTile(int x, int y) {
        return getGridTile(x, y).getTile();
    }

    public void setTile(int x, int y, ITile tile) {
        if (tiles[x][y] == null) tiles[x][y] = new GridTile(x, y);
        tiles[x][y].setTile(tile);
    }

    private void populate(boolean border) {
        for (int x = 0; x < this.sizeX; x++) {
            for (int y = 0; y < this.sizeY; y++) {
                if (border && (x == 0 || y == 0 || x == sizeX - 1 || y == sizeY - 1)) {
                    setTile(x, y, Tiles.WALL_TILE);
                } else {
                    setTile(x, y, Tiles.EMPTY_TILE);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void render(Graphics graphics) {
        var plr = Game.getInstance().getPlayer();
        var scale = Game.getInstance().getScale();

        var plrX = plr.getX();
        var plrY = plr.getY();
        var oX = 0;
        var oY = 0;

        if (plrX > boundsX)
            oX = plrX - boundsX;
        if (plrY > boundsY)
            oY = plrY - boundsY;

        for (int x = oX; x < Math.min(oX + boundsX + 2, sizeX); x++) {
            for (int y = oY; y < Math.min(oY + boundsY + 2, sizeY); y++) {
                var GridTile = getGridTile(x, y);
                var tile = GridTile.getTile();
                var tileEntity = GridTile.getTileEntity();

                var manager = RenderManager.getInstance();
                var renderer = manager.getRenderer(tile);
                if (renderer == null) continue;
                renderer.render(
                        graphics,
                        tile,
                        tileEntity,
                        x,
                        y,
                        oX,
                        oY,
                        scale,
                        scale
                );
            }
        }
    }
}
