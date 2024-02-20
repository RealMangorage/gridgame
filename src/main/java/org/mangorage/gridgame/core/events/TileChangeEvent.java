package org.mangorage.gridgame.core.events;

import org.mangorage.gridgame.core.grid.Grid;
import org.mangorage.gridgame.core.grid.tiles.Tile;
import org.mangorage.gridgame.core.grid.tiles.TilePos;

public class TileChangeEvent {
    private final Grid grid;
    private final TilePos pos;

    private Tile tile;
    private boolean cancelled;

    public TileChangeEvent(Grid grid, TilePos pos, Tile tile) {
        this.grid = grid;
        this.pos = pos;
        this.tile = tile;
    }

    public void setCancelled() {
        this.cancelled = true;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

    public TilePos getTilePos() {
        return pos;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
