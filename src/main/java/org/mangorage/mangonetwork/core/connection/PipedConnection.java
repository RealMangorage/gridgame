package org.mangorage.mangonetwork.core.connection;

import io.netty.channel.Channel;
import org.mangorage.mangonetwork.core.packet.IPacket;
import org.mangorage.mangonetwork.core.packet.PacketSender;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public final class PipedConnection implements IPipedConnection {
    private static final IConnection EMPTY_CONNECTION = new IConnection() {
        @Override
        public <T extends IPacket> void send(T packet) {
            System.out.println("Attemped to send packet. Unable to send. Empty Connection. Error");
        }
    };

    private final Map<InetSocketAddress, IConnection> connections = new ConcurrentHashMap<>();
    private final PacketSender packetSender;

    public PipedConnection(PacketSender sender) {
        this.packetSender = sender;
    }

    @Override
    public <T extends IPacket> void send(T packet, InetSocketAddress sendTo) {
        connections.forEach((k, v) -> v.send(packet));
    }

    @Override
    public <T extends IPacket> void send(T packet) {
        connections.forEach((k, v) -> v.send(packet));
    }

    @Override
    public Connection join(InetSocketAddress address, Supplier<Channel> channelSupplier) {
        if (connections.containsKey(address)) return null;
        var connection = new Connection(channelSupplier, address, packetSender);
        connections.put(address, connection);
        return connection;
    }
}