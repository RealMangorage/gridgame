package org.mangorage.mangonetwork.core;

import io.netty.channel.Channel;
import org.mangorage.mangonetwork.core.packet.IPacket;
import org.mangorage.mangonetwork.core.packet.PacketHandler;
import org.mangorage.mangonetwork.core.packet.PacketSender;

import java.net.InetSocketAddress;
import java.util.function.Supplier;

public class Connection {
    private final PacketSender packetSender;
    private final InetSocketAddress address;
    private final Supplier<Channel> channelSupplier;

    public Connection(Supplier<Channel> channelSupplier, InetSocketAddress address, Side side) {
        this.channelSupplier = channelSupplier;
        this.packetSender = new PacketSender(side);
        this.address = address;
    }


    @SuppressWarnings("all")
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
