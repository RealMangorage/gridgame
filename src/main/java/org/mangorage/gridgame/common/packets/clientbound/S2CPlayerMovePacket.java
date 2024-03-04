package org.mangorage.gridgame.common.packets.clientbound;

import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.mangonetwork.core.SimpleByteBuf;
import org.mangorage.mangonetwork.core.packet.Context;
import org.mangorage.mangonetwork.core.packet.IPacket;
import org.mangorage.mangonetwork.core.packet.PacketDirection;
import org.mangorage.mangonetwork.core.packet.PacketFlow;

@PacketDirection(flow = PacketFlow.CLIENTBOUND)
public class S2CPlayerMovePacket implements IPacket {
    private final TilePos pos;

    public S2CPlayerMovePacket(TilePos pos) {
        this.pos = pos;
    }

    public S2CPlayerMovePacket(SimpleByteBuf byteBuf) {
        this(new TilePos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt()));
    }

    public void encode(SimpleByteBuf buffer) {
        buffer.writeInt(pos.x());
        buffer.writeInt(pos.y());
        buffer.writeInt(pos.z());
    }

    // side -> The side that sent the packet, so if we get packets from Server, we are on a client
    // if we get packets from client, we are on server...
    public void handle(Context ctx) {
        GridGameClient.getInstance().getPlayer().moveTo(pos);
    }
}
