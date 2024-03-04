package org.mangorage.gridgame.common.packets;

import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.mangonetwork.core.Side;
import org.mangorage.mangonetwork.core.SimpleByteBuf;
import org.mangorage.mangonetwork.core.packet.Context;
import org.mangorage.mangonetwork.core.packet.IPacket;

public class WorldLoadPacket implements IPacket {
    private final int sizeX, sizeY, sizeZ;

    public WorldLoadPacket(int sizeX, int sizeY, int sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    public WorldLoadPacket(SimpleByteBuf byteBuf) {
        this(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
    }

    public void encode(SimpleByteBuf buffer) {
        buffer.writeInt(sizeX);
        buffer.writeInt(sizeY);
        buffer.writeInt(sizeZ);
    }

    public void handle(Context ctx) {
        if (ctx.from() == Side.SERVER) {
            GridGameClient.getInstance().getLevel().setTiles(sizeX, sizeY, sizeZ);
        }
    }
}
