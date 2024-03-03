package org.mangorage.mangonetwork;

import org.mangorage.gridgame.common.registry.GridGamePackets;
import org.mangorage.mangonetwork.core.packet.PacketHandler;
import org.mangorage.mangonetwork.packets.MessagePacket;

public class Packets {
    private static int ID = 0;

    public static final PacketHandler<MessagePacket> MESSAGE_PACKET = PacketHandler.create(MessagePacket.class, ID++);

    public static void init() {
        GridGamePackets.init();
    }
}
