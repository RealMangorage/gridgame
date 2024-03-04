package org.mangorage.gridgame.common.packets.clientbound;

import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.gridgame.common.Registries;
import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.mangonetwork.core.SimpleByteBuf;
import org.mangorage.mangonetwork.core.packet.Context;
import org.mangorage.mangonetwork.core.packet.IPacket;
import org.mangorage.mangonetwork.core.packet.PacketDirection;
import org.mangorage.mangonetwork.core.packet.PacketFlow;

@PacketDirection(flow = PacketFlow.CLIENTBOUND)
public class S2CTileUpdatePacket implements IPacket {
    private final int posX, posY, posZ;
    private final byte tileID;

    public S2CTileUpdatePacket(TilePos pos, byte ID) {
        this.posX = pos.x();
        this.posY = pos.y();
        this.posZ = pos.z();
        this.tileID = ID;
    }

    public S2CTileUpdatePacket(SimpleByteBuf byteBuf) {
        this(new TilePos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt()), byteBuf.readByte());
    }

    public void encode(SimpleByteBuf buffer) {
        buffer.writeInt(posX);
        buffer.writeInt(posY);
        buffer.writeInt(posZ);
        buffer.writeByte(tileID);
    }

    // side -> The side that sent the packet, so if we get packets from Server, we are on client
    // if we get packets from client, we are on server...
    public void handle(Context ctx) {
        GridGameClient.getInstance().getLevel().setTile(
                new TilePos(posX, posY, posZ),
                Registries.TILE_REGISTRY.getObject(tileID),
                Level.UPDATE_CLIENTS
        );
    }
}