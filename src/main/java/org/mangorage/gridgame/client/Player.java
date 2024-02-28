package org.mangorage.gridgame.client;

import org.mangorage.gridgame.common.packets.PlayerMovePacket;
import org.mangorage.gridgame.common.registry.TileRegistry;
import org.mangorage.gridgame.common.world.TilePos;

public class Player {
    private final ClientLevel level;
    private TilePos pos = new TilePos(0, 0, 1);

    public Player(ClientLevel level) {
        this.level = level;
        level.setTile(pos, TileRegistry.PLAYER_TILE.get(), 2);
    }

    public void setPos(TilePos pos) {
        level.setTile(this.pos, TileRegistry.EMPTY_TILE.get(), 2);
        this.pos = pos;
        level.setTile(pos, TileRegistry.PLAYER_TILE.get(), 2);
    }

    public void moveRight() {
        var newPos = new TilePos(pos.x() + 1, pos.y(), pos.z());
        var oldPos = pos;
        this.pos = newPos;
        GridGameClient.getInstance().getConnection().send(new PlayerMovePacket(oldPos, newPos));
    }

    public TilePos getPos() {
        return pos;
    }
}
