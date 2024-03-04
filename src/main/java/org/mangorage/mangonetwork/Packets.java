package org.mangorage.mangonetwork;

import org.mangorage.gridgame.common.packets.GridGamePackets;
import org.mangorage.mangonetwork.core.packet.PacketFlow;
import org.mangorage.mangonetwork.core.packet.PacketHandler;
import org.mangorage.mangonetwork.packets.MessagePacket;

public class Packets {
    private static int ID = 0;

    public static final PacketHandler<MessagePacket> MESSAGE_PACKET = PacketHandler.create(MessagePacket.class, ID++, PacketFlow.CLIENTBOUND);

    public static void init() {
        GridGamePackets.init();
    }
}
