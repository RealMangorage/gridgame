package org.mangorage.gridgame.common.world.entities;

import org.mangorage.gridgame.common.core.Direction;
import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.mangonetwork.core.connection.Connection;

public abstract class Player {
    private final Level level;
    private TilePos pos = new TilePos(0, 0, 1);

    public Player(Level level) {
        this.level = level;
    }


    abstract public void moveTo(TilePos pos);
    abstract public void move(Direction direction);
    abstract public Connection getConnection();

    public void setPos(TilePos pos) {
        this.pos = pos;
    }

    public TilePos getPos() {
        return pos;
    }

    public Level getLevel() {
        return level;
    }
}
