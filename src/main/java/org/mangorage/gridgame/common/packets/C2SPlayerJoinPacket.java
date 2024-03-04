package org.mangorage.gridgame.common.packets;

import org.mangorage.gridgame.server.GridGameServer;
import org.mangorage.mangonetwork.core.Side;
import org.mangorage.mangonetwork.core.SimpleByteBuf;
import org.mangorage.mangonetwork.core.packet.Context;
import org.mangorage.mangonetwork.core.packet.IPacket;

public class C2SPlayerJoinPacket implements IPacket {
    private final String username;

    public C2SPlayerJoinPacket(String username) {
        this.username = username;
    }

    public C2SPlayerJoinPacket(SimpleByteBuf byteBuf) {
        this(byteBuf.readString());
    }

    @Override
    public void encode(SimpleByteBuf buffer) {
        buffer.writeString(username);
    }

    @Override
    public void handle(Context ctx) {
        if (ctx.from() == Side.CLIENT) {
            GridGameServer.getInstance().addPlayer(ctx.socketAddress(), ctx.channel(), username);
        }
    }
}
