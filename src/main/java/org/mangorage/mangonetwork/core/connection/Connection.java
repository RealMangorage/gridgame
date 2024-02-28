package org.mangorage.mangonetwork.core.connection;

import io.netty.channel.Channel;
import org.mangorage.mangonetwork.core.packet.IPacket;
import org.mangorage.mangonetwork.core.packet.PacketHandler;
import org.mangorage.mangonetwork.core.packet.PacketSender;

import java.net.InetSocketAddress;
import java.util.function.Supplier;

public final class Connection implements IConnection {
    private final PacketSender packetSender;
    private final InetSocketAddress address;
    private final Supplier<Channel> channelSupplier;

    public Connection(Supplier<Channel> channelSupplier, InetSocketAddress address, PacketSender packetSender) {
        this.channelSupplier = channelSupplier;
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
                channelSupplier.get()
        );
    }
}
