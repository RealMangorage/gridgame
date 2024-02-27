package org.mangorage.gridgame.common.world;

import org.mangorage.mangonetwork.core.Side;

public abstract class TileEntity {
    private final Level level;
    private final TilePos pos;
    private final Side side;

    public TileEntity(Level level, TilePos pos, Side side) {
        this.level = level;
        this.pos = pos;
        this.side = side;
    }

    public Level getLevel() {
        return level;
    }

    public TilePos getPos() {
        return pos;
    }

    public Side getSide() {
        return side;
    }

    public boolean isClientSide() {
        return getSide() == Side.CLIENT;
    }

    public void tick() {

    }
}
