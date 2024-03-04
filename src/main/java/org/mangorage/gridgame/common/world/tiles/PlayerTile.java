package org.mangorage.gridgame.common.world.tiles;

import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.Tile;
import org.mangorage.gridgame.common.world.TileEntity;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.gridgame.common.world.tileentity.PlayerTileEntity;
import org.mangorage.mangonetwork.core.Side;

public class PlayerTile extends Tile {
    @Override
    public TileEntity createTileEntity(Level level, TilePos tilePos, Side side) {
        return new PlayerTileEntity(level, tilePos, side);
    }
}
