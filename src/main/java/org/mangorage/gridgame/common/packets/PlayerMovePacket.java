package org.mangorage.gridgame.common.packets;

import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.gridgame.common.registry.TileRegistry;
import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.gridgame.server.GridGameServer;
import org.mangorage.mangonetwork.core.Side;
import org.mangorage.mangonetwork.core.SimpleByteBuf;
import org.mangorage.mangonetwork.core.packet.IPacket;

import java.net.InetSocketAddress;

public class PlayerMovePacket implements IPacket {
    private final TilePos oldPos;
    private final TilePos newPos;

    public PlayerMovePacket(TilePos from, TilePos to) {
        this.oldPos = from;
        this.newPos = to;
    }

    public PlayerMovePacket(SimpleByteBuf byteBuf) {
        this(new TilePos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt()), new TilePos(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt()));
    }

    public void encode(SimpleByteBuf buffer) {
        buffer.writeInt(oldPos.x());
        buffer.writeInt(oldPos.y());
        buffer.writeInt(oldPos.z());

        buffer.writeInt(newPos.x());
        buffer.writeInt(newPos.y());
        buffer.writeInt(newPos.z());
    }

    // side -> The side that sent the packet, so if we get packets from Server, we are on client
    // if we get packets from client, we are on server...
    public void handle(InetSocketAddress origin, Side from) {
        if (from == Side.CLIENT) {
            var tile = GridGameServer.getInstance().getLevel().getTile(new TilePos(newPos.x(), newPos.z(), 0));

            if (tile == TileRegistry.EMPTY_TILE.get()) {
                GridGameServer.getInstance().getLevel().setTile(
                        oldPos,
                        TileRegistry.EMPTY_TILE.get(),
                        Level.UPDATE_CLIENTS
                );

                GridGameServer.getInstance().getLevel().setTile(
                        newPos,
                        TileRegistry.PLAYER_TILE.get(),
                        Level.UPDATE_CLIENTS
                );
            } else {
                var plr = GridGameServer.getInstance().getPlayer(origin);
                if (plr != null) {
                    plr.send(new PlayerMovePacket(newPos, oldPos)); // Put the player back...
                }
            }
        } else if (from == Side.SERVER) {
            GridGameClient.getInstance().getPlayer().setPos(newPos);
        }
    }
}
