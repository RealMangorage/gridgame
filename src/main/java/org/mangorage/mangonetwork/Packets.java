package org.mangorage.mangonetwork;

import org.mangorage.mangonetwork.core.packet.PacketHandler;
import org.mangorage.mangonetwork.packets.MessagePacket;

public class Packets {
    private static int ID = 0;

    public static final PacketHandler<MessagePacket> MESSAGE_PACKET = PacketHandler.create(
            MessagePacket.class,
            ID++,
            MessagePacket::encode,
            MessagePacket::new,
            MessagePacket::handle
    );

    public static void init() {
    }
}
