package org.mangorage.gridgame.common.world;

public abstract class Level {
    public static final int UPDATE_ALL = 1;
    public static final int UPDATE_CLIENTS = 2;

    abstract public int getSizeX();
    abstract public int getSizeY();
    abstract public int getSizeZ();

    abstract public void setTile(TilePos pos, Tile tile, int flag);
    abstract protected void tick();
}