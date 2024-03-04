package org.mangorage.mangonetwork.core.connection;

import io.netty.channel.Channel;
import org.mangorage.mangonetwork.core.packet.IPacket;
import org.mangorage.mangonetwork.core.packet.PacketHandler;
import org.mangorage.mangonetwork.core.packet.PacketSender;

import java.net.InetSocketAddress;
public final class Connection implements IConnection {
    private final PacketSender packetSender;
    private final InetSocketAddress address;
    private final Channel channel;

    public Connection(Channel channel, InetSocketAddress address, PacketSender packetSender) {
        this.channel = channel;
        this.packetSender = packetSender;
        this.address = address;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    @SuppressWarnings("all")
    @Override
    public <T extends IPacket> void send(T packet) {
        PacketHandler<IPacket> iPacket = (PacketHandler<IPacket>) PacketHandler.get(packet.getClass());
        iPacket.send(
                packet,
                packetSender,
                address,
                channel
        );
    }
}
