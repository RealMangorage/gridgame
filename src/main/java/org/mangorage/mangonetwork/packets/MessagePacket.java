package org.mangorage.mangonetwork.packets;

import org.mangorage.mangonetwork.core.packet.Context;
import org.mangorage.mangonetwork.core.packet.IPacket;
import org.mangorage.mangonetwork.core.SimpleByteBuf;

public class MessagePacket implements IPacket {
    private final String message;

    public MessagePacket(SimpleByteBuf buffer) {
        this(buffer.readString());
    }

    public MessagePacket(String message) {
        this.message = message;
    }

    public void encode(SimpleByteBuf buffer) {
        buffer.writeString(message);
    }

    // side -> The side that sent the packet, so if we get packets from Server, we are on client
    // if we get packets from client, we are on server...
    public void handle(Context ctx) {
        System.out.println("Message Recieved: " + message);
        System.out.println("side: " + ctx.from());
    }
}
