package org.mangorage.gridgame.common.world.tileentity;

import org.mangorage.gridgame.common.registry.TileRegistry;
import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.TileEntity;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.mangonetwork.core.Side;

public class SolidWallTileEntity extends TileEntity {
    private int ticks = 0;

    public SolidWallTileEntity(Level level, TilePos pos, Side side) {
        super(level, pos, side);
    }

    @Override
    public void tick() {
        if (getSide() == Side.SERVER) {
            ticks++;
            var maxY = getLevel().getSizeY();
            var pos = getPos();
            if (pos.y() + 1 < maxY && ticks % 20 == 0) {
                getLevel().setTile(pos, TileRegistry.EMPTY_TILE.get(), 1);
                getLevel().setTile(
                        new TilePos(
                                pos.x(),
                                pos.y() + 1,
                                pos.z()
                        ),
                        TileRegistry.SOLD_TILE.get(),
                        1
                );
            }
        }
    }
}
