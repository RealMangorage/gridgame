package org.mangorage.gridgame.server;

import io.netty.channel.Channel;
import org.mangorage.gridgame.common.packets.clientbound.S2CWorldLoadPacket;
import org.mangorage.gridgame.common.registry.TileRegistry;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.gridgame.common.world.entities.Player;
import org.mangorage.gridgame.server.world.ServerLevel;
import org.mangorage.gridgame.server.world.entities.ServerPlayer;
import org.mangorage.mangonetwork.core.connection.IPipedConnection;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GridGameServer {
    private static GridGameServer INSTANCE;

    public static void init(IPipedConnection pipedConnection) {
        //if (GridGameClient.getInstance() != null)
           // throw new IllegalStateException("Cannot start Server... Client already started");

        if (INSTANCE != null) return;

        INSTANCE = new GridGameServer(pipedConnection);
    }

    public static GridGameServer getInstance() {
        return INSTANCE;
    }

    private final ServerLevel serverLevel = new ServerLevel(10, 10, 2);
    private final IPipedConnection pipedConnection;
    private final Map<InetSocketAddress, Player> PLAYERS = new ConcurrentHashMap<>();

    private GridGameServer(IPipedConnection connection) {
        this.pipedConnection = connection;
    }


    public void addPlayer(InetSocketAddress address, Channel channel, String username) {
        var connection = pipedConnection.join(address, channel);
        if (connection != null) {
            PLAYERS.put(address, new ServerPlayer(serverLevel, username, connection));
            pipedConnection.send(new S2CWorldLoadPacket(serverLevel.getSizeX(), serverLevel.getSizeY(), serverLevel.getSizeZ()), address);
            serverLevel.setTile(new TilePos(0, 0, 0), TileRegistry.SOLD_TILE.get(), 2);
        }
    }

    public ServerLevel getLevel() {
        return serverLevel;
    }

    public IPipedConnection getPipedConnection() {
        return pipedConnection;
    }

    public Player getPlayer(InetSocketAddress playerAddress) {
        return PLAYERS.get(playerAddress);
    }
}
