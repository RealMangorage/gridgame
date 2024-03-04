package org.mangorage.gridgame.server.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import org.mangorage.gridgame.common.Registries;
import org.mangorage.gridgame.common.packets.clientbound.S2CTileUpdatePacket;
import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.Tile;
import org.mangorage.gridgame.common.world.TileEntity;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.gridgame.server.GridGameServer;
import org.mangorage.mangonetwork.core.LogicalSide;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerLevel extends Level {
    private final ScheduledExecutorService RUNNER = Executors.newScheduledThreadPool(1);
    private final byte[][][] tiles;
    private final int sizeX, sizeY, sizeZ;

    private final Long2ObjectArrayMap<TileEntity> TILE_ENTITYS = new Long2ObjectArrayMap<>();

    public ServerLevel(int sizeX, int sizeY, int sizeZ) {
        this.tiles = new byte[sizeZ][sizeX][sizeY];
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;

        RUNNER.scheduleAtFixedRate(this::tick, 0, (long)((1D / 20D) * 1000), TimeUnit.MILLISECONDS);
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    @Override
    public boolean isValid(TilePos pos) {
        if (pos.x() < 0 || pos.x() > getSizeX() - 1) return false;
        if (pos.y() < 0 || pos.y() > getSizeY() - 1) return false;
        return true;
    }

    @Override
    public void setTile(TilePos pos, Tile tile, int flag) {
        var id = Registries.TILE_REGISTRY.getID(tile);
        this.tiles[pos.z()][pos.x()][pos.z()] = id;

        var TE = tile.createTileEntity(this, pos, LogicalSide.SERVER);
        var packedPos = TilePos.pack(pos.x(), pos.y(), pos.z());
        TILE_ENTITYS.remove(packedPos);
        if (TE != null)
            TILE_ENTITYS.put(packedPos, TE);

        GridGameServer.getInstance().getPipedConnection().send(new S2CTileUpdatePacket(pos, id));
    }

    @Override
    public Tile getTile(TilePos pos) {
        return Registries.TILE_REGISTRY.getObject(tiles[pos.z()][pos.x()][pos.y()]);
    }

    @Override
    public TileEntity getTileEntity(TilePos pos) {
        return TILE_ENTITYS.get(TilePos.pack(pos));
    }

    @Override
    protected void tick() {
        TILE_ENTITYS.forEach((k, e) -> e.preTick());
    }
}
