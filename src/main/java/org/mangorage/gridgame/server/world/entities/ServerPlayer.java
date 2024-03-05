package org.mangorage.gridgame.server.world.entities;

import org.mangorage.gridgame.common.core.Direction;
import org.mangorage.gridgame.common.packets.clientbound.S2CPlayerMovePacket;
import org.mangorage.gridgame.common.registry.TileRegistry;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.gridgame.common.world.entities.Player;
import org.mangorage.gridgame.common.world.tileentity.PlayerTileEntity;
import org.mangorage.gridgame.server.world.ServerLevel;
import org.mangorage.mangonetwork.core.connection.Connection;

public final class ServerPlayer extends Player {
    private final Connection connection;

    public ServerPlayer(ServerLevel level, String username, Connection connection) {
        super(level, username);
        this.connection = connection;
    }

    @Override
    public void moveTo(TilePos pos) {
        if (!getLevel().isValid(pos)) return;
        getLevel().setTile(getPos(), TileRegistry.EMPTY_TILE.get(), 2);
        setPos(pos);
        getLevel().setTile(getPos(), TileRegistry.PLAYER_TILE.get(), 2);

        connection.send(new S2CPlayerMovePacket(getPos()));

        var TE = getLevel().getTileEntity(getPos());
        if (TE != null && TE instanceof PlayerTileEntity PTE)
            PTE.setUsername(getUsername());
    }

    @Override
    public void move(Direction direction) {
        var pos = getPos();
        switch (direction) {
            case UP -> moveTo(new TilePos(pos.x(), pos.y() - 1, pos.z()));
            case DOWN -> moveTo(new TilePos(pos.x(), pos.y() + 1, pos.z()));
            case LEFT -> moveTo(new TilePos(pos.x() - 1, pos.y(), pos.z()));
            case RIGHT -> moveTo(new TilePos(pos.x() + 1, pos.y(), pos.z()));
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public ServerLevel getLevel() {
        return (ServerLevel) super.getLevel();
    }
}
