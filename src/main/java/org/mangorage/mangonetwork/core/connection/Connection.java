package org.mangorage.mangonetwork.core.connection;

import io.netty.channel.Channel;
import org.mangorage.mangonetwork.core.packet.IPacket;
import org.mangorage.mangonetwork.core.packet.PacketFlow;
import org.mangorage.mangonetwork.core.packet.PacketHandler;
import java.net.InetSocketAddress;

public final class Connection implements IConnection {
    private final InetSocketAddress address;
    private final Channel channel;
    private final PacketFlow packetFlow;

    public Connection(Channel channel, InetSocketAddress address, PacketFlow packetFlow) {
        this.channel = channel;
        this.address = address;
        this.packetFlow = packetFlow;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    @SuppressWarnings("all")
    @Override
    public <T extends IPacket> void send(T packet) {
        var iPacket = PacketHandler.cast(PacketHandler.get(packet.getClass()));
        iPacket.send(
                packet,
                packetFlow,
                address,
                channel
        );
    }
}
