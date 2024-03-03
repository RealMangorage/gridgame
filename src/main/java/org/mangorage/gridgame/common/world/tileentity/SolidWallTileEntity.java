package org.mangorage.gridgame.common.world.tileentity;

import net.querz.nbt.tag.CompoundTag;
import org.mangorage.gridgame.common.packets.S2CTileEntityUpdatePacket;
import org.mangorage.gridgame.common.registry.TileRegistry;
import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.TileEntity;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.mangonetwork.core.Side;

public class SolidWallTileEntity extends TileEntity {
    private int ticks = 0;
    private boolean doTick = false;
    private boolean useCobble = false;

    public SolidWallTileEntity(Level level, TilePos pos, Side side) {
        super(level, pos, side);
    }

    public boolean isUseCobble() {
        return useCobble;
    }

    @Override
    public void tick() {
        if (getSide() == Side.SERVER) {
            ticks++;
            if (ticks % 20 == 0) {
                useCobble = !useCobble;
                markDirty();
            }
        }


        if (getSide() == Side.SERVER && doTick) {
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

    @Override
    public S2CTileEntityUpdatePacket getUpdatePacket() {
        return new S2CTileEntityUpdatePacket(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("cobble", useCobble);
        return tag;
    }

    @Override
    public void loadUpdateTag(CompoundTag tag) {
        this.useCobble = tag.getBoolean("cobble");
    }
}
