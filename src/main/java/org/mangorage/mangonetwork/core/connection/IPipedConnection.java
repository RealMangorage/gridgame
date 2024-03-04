package org.mangorage.mangonetwork.core.connection;

import io.netty.channel.Channel;
import org.mangorage.mangonetwork.core.packet.IPacket;
import java.net.InetSocketAddress;

public interface IPipedConnection extends IConnection {
    <T extends IPacket> void send(T packet, InetSocketAddress sendTo);
    Connection join(InetSocketAddress address, Channel channel);
}
