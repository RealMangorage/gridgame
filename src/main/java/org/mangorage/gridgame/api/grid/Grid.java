package org.mangorage.gridgame.api.grid;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.mangorage.gridgame.registry.TileRegistry;
import org.mangorage.gridgame.render.RenderManager;
import org.mangorage.gridgame.game.Game;
import org.mangorage.gridgame.registry.Tiles;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Grid {
    // for Querying a specific x/y coordinate
    private final GridTile[][] tiles;

    // for general looping, if you want to go through all the tiles and tick them
    private final List<GridTile> tilesList;
    private final int sizeX, sizeY;

    private int boundsX, boundsY;
    private Grid nextLayer;

    public Grid(int x, int y, int boundsX, int boundsY, boolean border) {
        this.sizeX = x;
        this.sizeY = y;
        this.boundsX = boundsX;
        this.boundsY = boundsY;
        this.tiles = new GridTile[x][y];
        populate(border);

        List<GridTile> tileList = new ArrayList<>();
        for (GridTile[] tile : tiles) {
            tileList.addAll(Arrays.asList(tile));
        }

        this.tilesList = List.copyOf(tileList);
    }

    public Grid(int x, int y, int bX, int bY) {
        this(x, y, bX, bY, true);
    }

    public void tick() {
        tilesList.forEach(gridTile -> {
            var tileEntity = gridTile.getTileEntity();
            if (tileEntity != null)
                tileEntity.tick();
        });

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

    public void save(CompoundTag tag) {
        ListTag<CompoundTag> tilesTags = new ListTag<>(CompoundTag.class);
        var map = TileRegistry.getInstance().getTileLookup();

        tilesList.forEach(gridTile -> {
            if (gridTile.getTile().canSave()) {
                CompoundTag tileTag = new CompoundTag();
                String id = TileRegistry.getInstance().getID(gridTile.getTile());
                int x = gridTile.getX();
                int y = gridTile.getY();

                tileTag.putShort("id", map.get(id));
                tileTag.putInt("x", x);
                tileTag.putInt("y", y);

                // TODO: Tack on TileData
                var TE = gridTile.getTileEntity();
                if (TE != null) {
                    CompoundTag tileDataTag = new CompoundTag();
                    TE.save(tileDataTag);
                    tileTag.put("tileData", tileDataTag);
                }

                tilesTags.add(tileTag);
            }
        });
        tag.put("tiles", tilesTags);
    }

    public void load(CompoundTag tag, Map<Short, String> tileLookup) {
        ListTag<CompoundTag> tilesTags = tag.getListTag("tiles").asCompoundTagList();

        tilesTags.forEach(tile -> {
            String id = tileLookup.get(tile.getShort("id"));
            var x = tile.getInt("x");
            var y = tile.getInt("y");

            getGridTile(x, y).setTile(TileRegistry.getInstance().getTile(id));
            var TE = getGridTile(x, y).getTileEntity();
            if (TE != null && tile.containsKey("tileData")) {
                TE.load(tile.getCompoundTag("tileData"));
            }
        });
    }
}
