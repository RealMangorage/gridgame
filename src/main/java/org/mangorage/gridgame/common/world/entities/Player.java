package org.mangorage.gridgame.common.world.entities;

import org.mangorage.gridgame.common.core.Direction;
import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.mangonetwork.core.connection.IConnection;

public abstract class Player {
    private final Level level;
    private final String username;
    private TilePos pos = new TilePos(0, 0, 1);

    public Player(Level level, String username) {
        this.level = level;
        this.username = username;
    }


    abstract public void moveTo(TilePos pos);
    abstract public void move(Direction direction);
    abstract public IConnection getConnection();

    public void setPos(TilePos pos) {
        this.pos = pos;
    }

    public TilePos getPos() {
        return pos;
    }

    public Level getLevel() {
        return level;
    }

    public String getUsername() {
        return username;
    }
}
