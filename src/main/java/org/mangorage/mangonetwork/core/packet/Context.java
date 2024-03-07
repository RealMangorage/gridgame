package org.mangorage.mangonetwork.core.packet;

import io.netty.channel.Channel;
import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.gridgame.common.world.entities.Player;
import org.mangorage.gridgame.server.GridGameServer;
import org.mangorage.mangonetwork.core.connection.IConnection;

public record Context(IConnection recipient, Channel channel, PacketFlow packetFlow) {
    public Player getPlayer() {
        if (packetFlow == PacketFlow.SERVERBOUND) {
            return GridGameServer.getInstance().getPlayer(recipient.getAddress());
        } else {
            return GridGameClient.getInstance().getPlayer();
        }
    }
}
