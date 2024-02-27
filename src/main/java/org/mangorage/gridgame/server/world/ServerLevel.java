package org.mangorage.gridgame.server.world;

import org.mangorage.gridgame.common.Registries;
import org.mangorage.gridgame.common.packets.TileUpdatePacket;
import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.Tile;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.gridgame.server.GridGameServer;

public class ServerLevel extends Level {
    private final byte[][][] tiles;
    private final int sizeX, sizeY, sizeZ;

    public ServerLevel(int sizeX, int sizeY, int sizeZ) {
        this.tiles = new byte[sizeZ][sizeX][sizeY];
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
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
    public void setTile(TilePos pos, Tile tile, int flag) {
        var id = Registries.TILE_REGISTRY.getID(tile);
        this.tiles[pos.z()][pos.x()][pos.z()] = id;

        GridGameServer.getInstance().getPlayers().forEach(plr -> {
            System.out.println("SENT PACKET CHANGE");
            plr.send(new TileUpdatePacket(pos, id));
        });
    }
}
