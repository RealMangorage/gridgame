package org.mangorage.gridgame.core.grid;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.mangorage.gridgame.core.events.Events;
import org.mangorage.gridgame.core.events.TileChangeEvent;
import org.mangorage.gridgame.core.grid.tiles.GridTile;
import org.mangorage.gridgame.core.grid.tiles.IEntityTile;
import org.mangorage.gridgame.core.grid.tiles.Tile;
import org.mangorage.gridgame.core.grid.tiles.TileEntity;
import org.mangorage.gridgame.core.grid.tiles.TilePos;
import org.mangorage.gridgame.game.Player;
import org.mangorage.gridgame.registry.Tiles;
import org.mangorage.gridgame.core.registry.Registries;
import org.mangorage.gridgame.core.render.TileRendererManager;
import org.mangorage.gridgame.game.Game;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Grid {
    // for Querying a specific x/y coordinate
    private final byte[][][] tiles;
    private final TileEntity[][][] tile_entities;
    private final BoundTickableTileEntity<?>[][][] tile_entities_tickable;

    private final ExecutorService executor;

    private final int sizeX, sizeY, sizeZ;
    private int boundsX, boundsY;

    private int ticked = 0;

    public Grid(int x, int y, int z, int boundsX, int boundsY, boolean border) {
        this.sizeX = x;
        this.sizeY = y;
        this.sizeZ = z;
        this.boundsX = boundsX;
        this.boundsY = boundsY;
        this.tiles = new byte[z][x][y];
        this.tile_entities = new TileEntity[z][x][y];
        this.tile_entities_tickable = new BoundTickableTileEntity[z][x][y];

        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        populate(border);
    }

    public Grid(int x, int y, int z, int bX, int bY) {
        this(x, y, z, bX, bY, true);
    }

    public void tick() {
        ticked = 0;

        for (BoundTickableTileEntity<?>[][] boundTickableTileEntities : tile_entities_tickable) {
            for (BoundTickableTileEntity<?>[] boundTickableTileEntity : boundTickableTileEntities) {
                for (BoundTickableTileEntity<?> tickableTileEntity : boundTickableTileEntity) {
                    if (tickableTileEntity != null) {
                        tickableTileEntity.tick();
                        ticked++;
                    }
                }
            }
        }

    }

    public void updateBounds(int x, int y) {
        if (x > 5)
            this.boundsX = Math.min(x, sizeX);
        if (y > 5)
            this.boundsY = Math.min(sizeY, y);
    }


    public byte[][][] getGridData() {
        return tiles;
    }

    public int getTickedTE() {
        return ticked;
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

    public int getOffsetX(Player player) {
        var plrX = player.getX();
        return plrX > boundsX ? plrX - boundsX : 0;
    }

    public int getOffsetY(Player player) {
        var plrY = player.getY();
        return plrY > boundsX ? plrY - boundsY : 0;
    }


    public GridTile getGridTile(int x, int y, int z) {
        return new GridTile(
                    new TilePos(x, y, z),
                    Registries.TILE_REGISTRY.getObject(tiles[z][x][y]),
                    tile_entities[z][x][y]
                );
    }


    public GridTile setTile(int x, int y, int z, Tile tile) {
        var event = new TileChangeEvent(this, new TilePos(x, y, z), tile);
        Events.TILE_CHANGE_EVENT.trigger(event);
        if (event.isCancelled()) return null;

        tile = event.getTile();

        tiles[z][x][y] = Registries.TILE_REGISTRY.getID(tile);

        tile_entities[z][x][y] = null;
        tile_entities_tickable[z][x][y] = null;

        var pos = new TilePos(x, y, z);

        if (tile instanceof IEntityTile entityTile) {
            var entity = entityTile.createTileEntity(this, x, y, z);
            tile_entities[z][x][y] = entity;
            var tickable = entityTile.getTicker();
            if (tickable != null)
                tile_entities_tickable[z][x][y] = new BoundTickableTileEntity<>(tickable, this, pos, entity);
            return new GridTile(
                    pos,
                    tile,
                    entity
            );
        }

        return new GridTile(
                new TilePos(x, y, z),
                tile,
                null
        );
    }

    private void populate(boolean border) {
        for (int x = 0; x < this.sizeX; x++) {
            for (int y = 0; y < this.sizeY; y++) {
                if (border && (x == 0 || y == 0 || x == sizeX - 1 || y == sizeY - 1)) {
                    setTile(x, y, 0, Tiles.WALL_TILE.get());
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

        for (int z = 0; z < sizeZ; z++) {
            for (int x = oX; x < Math.min(oX + boundsX + 3, sizeX); x++) {
                for (int y = oY; y < Math.min(oY + boundsY + 3, sizeY); y++) {
                    var GridTile = getGridTile(x, y, z);
                    var tile = GridTile.getTile();
                    var tileEntity = GridTile.getTileEntity();

                    var manager = TileRendererManager.getInstance();
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

    public void load(byte[][][] data) {
        for (int z = 0; z < sizeZ; z++) {
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    setTile(x, y, z, Registries.TILE_REGISTRY.getObject(data[z][x][y]));
                }
            }
        }

    }

    public void save(CompoundTag tag) {
        ListTag<CompoundTag> tilesTags = new ListTag<>(CompoundTag.class);
        for (int z = 0; z < sizeZ; z++) {
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    var tile = getGridTile(x, y, z);
                    if (tile.getTile().canSave() && tile.getTileEntity() != null) {
                        var packedPos = TilePos.pack(tile.getX(), tile.getY(), tile.getZ());
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
        }

        tag.put("tiles", tilesTags);
    }

    public void load(CompoundTag tag) {
        ListTag<CompoundTag> tilesTags = tag.getListTag("tiles").asCompoundTagList();

        tilesTags.forEach(tile -> {
            var pos = TilePos.unPack(tile.getLong("packedPos"));
            var gridTile = getGridTile(pos.x(), pos.y(), pos.z());
            if (tile.containsKey("tileData") && gridTile.getTileEntity() != null)
                gridTile.getTileEntity().load(tile.getCompoundTag("tileData"));
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grid grid = (Grid) o;
        return sizeX == grid.sizeX && sizeY == grid.sizeY && sizeZ == grid.sizeZ && boundsX == grid.boundsX && boundsY == grid.boundsY && Objects.deepEquals(tiles, grid.tiles) && Objects.equals(tile_entities, grid.tile_entities) && Objects.equals(tile_entities_tickable, grid.tile_entities_tickable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(tiles), tile_entities, tile_entities_tickable, sizeX, sizeY, sizeZ, boundsX, boundsY);
    }
}
