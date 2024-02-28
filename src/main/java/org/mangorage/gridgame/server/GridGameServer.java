package org.mangorage.gridgame.server;

import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.gridgame.common.BootStrap;
import org.mangorage.gridgame.common.packets.WorldLoadPacket;
import org.mangorage.gridgame.common.registry.TileRegistry;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.gridgame.server.world.ServerLevel;
import org.mangorage.mangonetwork.core.Connection;
import org.mangorage.mangonetwork.core.Scheduler;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GridGameServer {
    private static GridGameServer INSTANCE;

    public static void init() {
        if (GridGameClient.getInstance() != null)
            throw new IllegalStateException("Cannot start Server... Client already started");

        INSTANCE = new GridGameServer();

        BootStrap.init(true);
    }

    public static GridGameServer getInstance() {
        return INSTANCE;
    }

    private final List<Connection> PLAYERS = new ArrayList<>();
    private final ServerLevel serverLevel = new ServerLevel(10, 10, 2);

    public void addPlayer(Connection connection) {
        PLAYERS.add(connection);

        connection.send(new WorldLoadPacket(serverLevel.getSizeX(), serverLevel.getSizeY(), serverLevel.getSizeZ()));
    }

    public ServerLevel getLevel() {
        return serverLevel;
    }

    public List<Connection> getPlayers() {
        return PLAYERS;
    }

    public Connection getPlayer(InetSocketAddress address) {
        var optional = getPlayers().stream().filter(c -> c.getAddress().equals(address)).findAny();
        return optional.isPresent() ? optional.get() : null;
    }
}
