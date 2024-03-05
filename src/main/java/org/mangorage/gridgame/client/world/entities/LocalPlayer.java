package org.mangorage.gridgame.client.world.entities;

import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.gridgame.client.world.ClientLevel;
import org.mangorage.gridgame.common.core.Direction;
import org.mangorage.gridgame.common.packets.serverbound.C2SPlayerMovePacket;
import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.gridgame.common.world.entities.Player;
import org.mangorage.gridgame.common.world.tileentity.PlayerTileEntity;
import org.mangorage.mangonetwork.core.connection.Connection;

public final class LocalPlayer extends Player {

    public LocalPlayer(Level level, String username) {
        super(level, username);
    }

    @Override
    public void moveTo(TilePos pos) {
        var TE = getLevel().getTileEntity(pos);
        if (TE instanceof PlayerTileEntity PTE)
            PTE.setUsername(getUsername());
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
