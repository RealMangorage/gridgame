package org.mangorage.gridgame.server;

import io.netty.channel.Channel;
import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.gridgame.common.BootStrap;
import org.mangorage.gridgame.common.packets.WorldLoadPacket;
import org.mangorage.gridgame.common.registry.TileRegistry;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.gridgame.server.world.ServerLevel;
import org.mangorage.mangonetwork.core.connection.IPipedConnection;

import java.net.InetSocketAddress;
import java.util.function.Supplier;

public class GridGameServer {
    private static GridGameServer INSTANCE;

    public static void init(IPipedConnection pipedConnection) {
        if (GridGameClient.getInstance() != null)
            throw new IllegalStateException("Cannot start Server... Client already started");

        if (INSTANCE != null) return;

        INSTANCE = new GridGameServer(pipedConnection);

        BootStrap.init(true);
    }

    public static GridGameServer getInstance() {
        return INSTANCE;
    }

    private final ServerLevel serverLevel = new ServerLevel(10, 10, 2);
    private final IPipedConnection pipedConnection;

    private GridGameServer(IPipedConnection connection) {
        this.pipedConnection = connection;
    }


    public void addPlayer(InetSocketAddress address, Supplier<Channel> channelSupplier) {
        if (!pipedConnection.join(address, channelSupplier)) {
            pipedConnection.send(new WorldLoadPacket(serverLevel.getSizeX(), serverLevel.getSizeY(), serverLevel.getSizeZ()), address);
            serverLevel.setTile(new TilePos(0, 0, 0), TileRegistry.SOLD_TILE.get(), 2);
        }
    }

    public ServerLevel getLevel() {
        return serverLevel;
    }

    public IPipedConnection getPipedConnection() {
        return pipedConnection;
    }
}
