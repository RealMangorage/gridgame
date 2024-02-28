package org.mangorage.mangonetwork.core.connection;

import org.mangorage.mangonetwork.core.packet.IPacket;

public interface IConnection {
    <T extends IPacket> void send(T packet);
}
