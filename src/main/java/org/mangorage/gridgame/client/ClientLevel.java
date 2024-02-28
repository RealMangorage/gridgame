package org.mangorage.gridgame.client;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import org.mangorage.gridgame.client.core.TileRendererManager;
import org.mangorage.gridgame.common.Registries;
import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.Tile;
import org.mangorage.gridgame.common.world.TileEntity;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.mangonetwork.core.Scheduler;
import org.mangorage.mangonetwork.core.Side;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class ClientLevel extends Level {
    private byte[][][] tiles;
    private int sizeX, sizeY, sizeZ;

    private final Long2ObjectArrayMap<TileEntity> TILE_ENTITYS = new Long2ObjectArrayMap<>();


    public ClientLevel() {
        Scheduler.RUNNER.scheduleAtFixedRate(this::tick, 0, (long)((1D / 20D) * 1000), TimeUnit.MILLISECONDS);
    }

    public void setTiles(int x, int y, int z) {
        if (tiles == null) {
            this.tiles = new byte[z][x][y];
            this.sizeX = x;
            this.sizeY = y;
            this.sizeZ = z;
        }
    }

    @Override
    public Tile getTile(TilePos pos) {
        return Registries.TILE_REGISTRY.getObject(tiles[pos.z()][pos.x()][pos.y()]);
    }

    @Override
    public int getSizeX() {
        return sizeX;
    }

    @Override
    public int getSizeY() {
        return sizeY;
    }

    @Override
    public int getSizeZ() {
        return sizeZ;
    }

    @Override
    public void setTile(TilePos pos, Tile tile, int flag) {
        if (tiles == null) return;
        if (flag == 2) {
            tiles[pos.z()][pos.x()][pos.y()] = Registries.TILE_REGISTRY.getID(tile);

            var TE = tile.createTileEntity(this, pos, Side.SERVER);
            var packedPos = TilePos.pack(pos.x(), pos.y(), pos.z());
            TILE_ENTITYS.remove(packedPos);
            if (TE != null)
                TILE_ENTITYS.put(packedPos, TE);
        }
    }



    @Override
    protected void tick() {
        TILE_ENTITYS.forEach((k, e) -> e.tick());
    }

    public void render(Graphics graphics) {
        if (tiles != null) {
            for (int z = 0; z < tiles.length; z++) {
                for (int x = 0; x < tiles[z].length ; x++) {
                    for (int y = 0; y < tiles[z][x].length; y++) {
                        var id = tiles[z][x][y];
                        var tile = Registries.TILE_REGISTRY.getObject(id);

                        TileRendererManager.ITileRenderer<Tile, TileEntity> renderer = GridGameClient.getInstance().getRenderManager().getRenderer(tile);

                        if (renderer != null) {
                            var TE = TILE_ENTITYS.get(TilePos.pack(x, y, z));
                            renderer.render(graphics, tile, TE, x, y, 0, 0, 16, 16);
                        }
                    }
                }
            }
        }
    }
}
