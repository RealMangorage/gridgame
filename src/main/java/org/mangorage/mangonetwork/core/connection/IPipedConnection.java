package org.mangorage.mangonetwork.core.connection;

import io.netty.channel.Channel;
import org.mangorage.mangonetwork.core.packet.IPacket;

import java.net.InetSocketAddress;

public interface IPipedConnection extends IConnection {
    <T extends IPacket> void send(T packet, InetSocketAddress sendTo);

    IConnection getOrCreate(InetSocketAddress address, Channel channel);

    @Override
    default InetSocketAddress getAddress() {
        return null;
    }
}
