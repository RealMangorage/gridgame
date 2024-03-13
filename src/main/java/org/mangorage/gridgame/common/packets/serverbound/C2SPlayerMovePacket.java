package org.mangorage.gridgame.common.packets.serverbound;

import org.mangorage.gridgame.common.core.Direction;
import org.mangorage.mangonetwork.core.SimpleByteBuf;
import org.mangorage.mangonetwork.core.packet.Context;
import org.mangorage.mangonetwork.core.packet.IPacket;
import org.mangorage.mangonetwork.core.packet.PacketDirection;
import org.mangorage.mangonetwork.core.packet.PacketFlow;

@PacketDirection(flow = PacketFlow.SERVERBOUND)
public class C2SPlayerMovePacket implements IPacket {
    private final Direction direction;

    public C2SPlayerMovePacket(Direction direction) {
        this.direction = direction;
    }

    public C2SPlayerMovePacket(SimpleByteBuf byteBuf) {
        this(Direction.values()[byteBuf.readInt()]);
    }

    public void encode(SimpleByteBuf buffer) {
        buffer.writeInt(direction.ordinal());
    }

    // side -> The side that sent the packet, so if we get packets from Server, we are on client
    // if we get packets from a client, we are on server...
    public void handle(Context ctx) {
        if (ctx.getPlayer() != null)
            ctx.getPlayer().move(direction);
    }
}
