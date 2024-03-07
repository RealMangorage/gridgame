package org.mangorage.mangonetwork.core.connection;

import org.mangorage.mangonetwork.core.packet.IPacket;

import java.net.InetSocketAddress;

public interface IConnection {
    <T extends IPacket> void send(T packet);
    InetSocketAddress getAddress();
}
