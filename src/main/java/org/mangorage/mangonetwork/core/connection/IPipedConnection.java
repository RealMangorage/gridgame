package org.mangorage.mangonetwork.core.connection;

import io.netty.channel.Channel;
import org.mangorage.mangonetwork.core.packet.IPacket;
import org.mangorage.mangonetwork.core.packet.PacketSender;

import java.net.InetSocketAddress;
import java.util.function.Supplier;

public interface IPipedConnection extends IConnection {
    <T extends IPacket> void send(T packet, InetSocketAddress sendTo);
    boolean join(InetSocketAddress address, Supplier<Channel>channelSupplier);
}
