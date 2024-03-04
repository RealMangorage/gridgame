package org.mangorage.gridgame.common.packets.clientbound;

import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.mangonetwork.core.SimpleByteBuf;
import org.mangorage.mangonetwork.core.packet.Context;
import org.mangorage.mangonetwork.core.packet.IPacket;
import org.mangorage.mangonetwork.core.packet.PacketFlow;

public class S2CWorldLoadPacket implements IPacket {
    private final int sizeX, sizeY, sizeZ;

    public S2CWorldLoadPacket(int sizeX, int sizeY, int sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    public S2CWorldLoadPacket(SimpleByteBuf byteBuf) {
        this(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
    }

    public void encode(SimpleByteBuf buffer) {
        buffer.writeInt(sizeX);
        buffer.writeInt(sizeY);
        buffer.writeInt(sizeZ);
    }

    public void handle(Context ctx) {
        GridGameClient.getInstance().getLevel().setTiles(sizeX, sizeY, sizeZ);
    }
}
