package org.mangorage.gridgame.common.packets;

import org.mangorage.gridgame.common.core.Direction;
import org.mangorage.gridgame.server.GridGameServer;
import org.mangorage.mangonetwork.core.Side;
import org.mangorage.mangonetwork.core.SimpleByteBuf;
import org.mangorage.mangonetwork.core.packet.Context;
import org.mangorage.mangonetwork.core.packet.IPacket;

import java.net.InetSocketAddress;

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
    // if we get packets from client, we are on server...
    public void handle(Context ctx) {
        if (ctx.from() == Side.CLIENT) {
            GridGameServer.getInstance().getPlayer(ctx.socketAddress()).move(direction);
        }
    }
}
