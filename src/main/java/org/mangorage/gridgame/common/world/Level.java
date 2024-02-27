package org.mangorage.gridgame.common.world;

public abstract class Level {
    public static final int UPDATE_ALL = 1;
    public static final int UPDATE_CLIENTS = 2;

    abstract public void setTile(TilePos pos, Tile tile, int flag);
}
