package org.mangorage.gridgame.client.world.entities;

import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.gridgame.client.world.ClientLevel;
import org.mangorage.gridgame.common.core.Direction;
import org.mangorage.gridgame.common.packets.serverbound.C2SPlayerMovePacket;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.gridgame.common.world.entities.Player;
import org.mangorage.mangonetwork.core.connection.Connection;

public final class LocalPlayer extends Player {
    public LocalPlayer(ClientLevel level) {
        super(level, "local");
    }

    @Override
    public void moveTo(TilePos pos) {
        /**
        getLevel().setTile(getPos(), TileRegistry.EMPTY_TILE.get(), 2);
        setPos(pos);
        getLevel().setTile(getPos(), TileRegistry.PLAYER_TILE.get(), 2);
        **/
    }

    @Override
    public void move(Direction direction) {
        getConnection().send(new C2SPlayerMovePacket(direction));
    }

    @Override
    public Connection getConnection() {
        return GridGameClient.getInstance().getConnection();
    }

    public ClientLevel getLevel() {
        return (ClientLevel) super.getLevel();
    }
}
