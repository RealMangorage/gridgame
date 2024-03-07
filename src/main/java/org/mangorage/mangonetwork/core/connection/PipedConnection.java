package org.mangorage.mangonetwork.core.connection;

import io.netty.channel.Channel;
import org.mangorage.mangonetwork.core.packet.IPacket;
import org.mangorage.mangonetwork.core.packet.PacketFlow;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Server Sided Connection
public final class PipedConnection implements IPipedConnection {
    private final Map<InetSocketAddress, IConnection> CONNECTIONS = new ConcurrentHashMap<>();

    public PipedConnection() {

    }

    @Override
    public <T extends IPacket> void send(T packet, InetSocketAddress sendTo) {
        CONNECTIONS.forEach((k, v) -> v.send(packet));
    }

    @Override
    public <T extends IPacket> void send(T packet) {
        CONNECTIONS.forEach((k, v) -> v.send(packet));
    }

    @Override
    public IConnection getOrCreate(InetSocketAddress address, Channel channel) {
        return CONNECTIONS.computeIfAbsent(address, a -> new Connection(channel, a, PacketFlow.CLIENTBOUND));
    }
}
