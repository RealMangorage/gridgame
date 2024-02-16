package org.mangorage.gridgame.api.grid;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.mangorage.gridgame.api.TilePos;
import org.mangorage.gridgame.registry.TileRegistry;
import org.mangorage.gridgame.render.RenderManager;
import org.mangorage.gridgame.game.Game;
import org.mangorage.gridgame.registry.Tiles;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Grid {
    // for Querying a specific x/y coordinate
    private final byte[][] tiles;
    private final Long2ObjectArrayMap<ITileEntity> tile_entities = new Long2ObjectArrayMap<>();

    // for general looping, if you want to go through all the tiles and tick them
    private final int sizeX, sizeY;

    private int boundsX, boundsY;
    private Grid nextLayer;

    public Grid(int x, int y, int boundsX, int boundsY, boolean border) {
        this.sizeX = x;
        this.sizeY = y;
        this.boundsX = boundsX;
        this.boundsY = boundsY;
        this.tiles = new byte[x][y];
        populate(border);
    }

    public Grid(int x, int y, int bX, int bY) {
        this(x, y, bX, bY, true);
    }

    public void tick() {
        tile_entities.forEach((aLong, iTileEntity) -> iTileEntity.tick());
        if (getNextLayer() != null) getNextLayer().tick();
    }

    public Grid createNewLayer() {
        if (this.nextLayer != null) return nextLayer;
        this.nextLayer = new Grid(sizeX, sizeY, boundsX, boundsY, false);
        return nextLayer;
    }

    public void updateBounds(int x, int y) {
        if (x > 5)
            this.boundsX = Math.min(x, sizeX);
        if (y > 5)
            this.boundsY = Math.min(sizeY, y);
        if (getNextLayer() != null) getNextLayer().updateBounds(x, y);
    }

    public Grid getNextLayer() {
        return nextLayer;
    }

    public byte[][] getGridData() {
        return tiles;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getBoundsX() {
        return boundsX;
    }

    public int getBoundsY() {
        return boundsY;
    }


    public GridTile getGridTile(int x, int y) {
        return new GridTile(
                x,
                y,
                TileRegistry.getInstance().getTile(tiles[x][y]),
                tile_entities.get(TilePos.pack(x, y))
                );
    }

    public GridTile setTile(int x, int y, ITile tile) {
        tiles[x][y] = TileRegistry.getInstance().getID(tile);
        var packedPos = TilePos.pack(x, y);
        if (tile instanceof IEntityTile<?> entityTile) {
            var entity = entityTile.createTileEntity(x, y);
            tile_entities.put(packedPos, entity);
            return new GridTile(
                    x,
                    y,
                    tile,
                    entity
            );
        } else {
            tile_entities.remove(packedPos);
        }

        return new GridTile(
                x,
                y,
                tile,
                null
        );
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

        for (int x = oX; x < Math.min(oX + boundsX + 3, sizeX); x++) {
            for (int y = oY; y < Math.min(oY + boundsY + 3, sizeY); y++) {
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

        if (getNextLayer() != null)
            getNextLayer().render(graphics);
    }

    public void load(byte[][] data) {
        for (int x = 0; x < data.length; x++) {
            for (int y = 0; y < data[x].length; y++) {
                setTile(x, y, TileRegistry.getInstance().getTile(data[x][y]));
            }
        }
    }

    public void save(CompoundTag tag) {
        ListTag<CompoundTag> tilesTags = new ListTag<>(CompoundTag.class);

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                var tile = getGridTile(x, y);
                if (tile.getTile().canSave() && tile.getTileEntity() != null) {
                    var packedPos = TilePos.pack(tile.getX(), tile.getY());
                    var TE = tile.getTileEntity();

                    CompoundTag tileTag = new CompoundTag();
                    tileTag.putLong("packedPos", packedPos);

                    CompoundTag tileEntityTag = new CompoundTag();
                    TE.save(tileEntityTag);
                    tileTag.put("tileData", tileEntityTag);

                    tilesTags.add(tileTag);
                }
            }
        }

        tag.put("tiles", tilesTags);
    }

    public void load(CompoundTag tag) {
        ListTag<CompoundTag> tilesTags = tag.getListTag("tiles").asCompoundTagList();

        tilesTags.forEach(tile -> {
            var pos = TilePos.unPack(tile.getLong("packedPos"));
            var gridTile = getGridTile(pos.x(), pos.y());
            if (tile.containsKey("tileData") && gridTile.getTileEntity() != null)
                gridTile.getTileEntity().load(tile.getCompoundTag("tileData"));
        });
    }
}
