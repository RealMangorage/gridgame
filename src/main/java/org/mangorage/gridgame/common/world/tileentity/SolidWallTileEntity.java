package org.mangorage.gridgame.common.world.tileentity;

import net.querz.nbt.tag.CompoundTag;
import org.mangorage.gridgame.common.packets.clientbound.S2CTileEntityUpdatePacket;
import org.mangorage.gridgame.common.registry.TileRegistry;
import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.TileEntity;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.mangonetwork.core.LogicalSide;

public class SolidWallTileEntity extends TileEntity {
    private int ticks = 0;
    private boolean doTick = false;
    private boolean useCobble = false;
    private int mode = 0;

    public SolidWallTileEntity(Level level, TilePos pos, LogicalSide logicalSide) {
        super(level, pos, logicalSide);
    }

    public boolean isUseCobble() {
        return useCobble;
    }

    @Override
    public void tick() {
        ticks++;
        if (!isClientSide()) {
            if (ticks % 20 == 0) {

                useCobble = !useCobble;
                markDirty();

                var maxY = getLevel().getSizeY() - 1;
                var pos = getPos();

                if (pos.y() < maxY && mode == 0) {
                    getLevel().setTile(pos, TileRegistry.EMPTY_TILE.get(), 1);
                    var newPos = new TilePos(pos.x(), pos.y() + 1, pos.z());
                    getLevel().setTile(newPos, TileRegistry.SOLD_TILE.get(), 1);
                    var TE = getLevel().getTileEntity(newPos);
                    if (TE instanceof SolidWallTileEntity STE) {
                        STE.ticks = this.ticks;
                        STE.useCobble = this.useCobble;
                        STE.mode = mode;
                        STE.markDirty();
                    }
                } else if (mode == 0) {
                    mode = 1;
                } else if (pos.y() > 0 && mode == 1) {
                    getLevel().setTile(pos, TileRegistry.EMPTY_TILE.get(), 1);
                    var newPos = new TilePos(pos.x(), pos.y() - 1, pos.z());
                    getLevel().setTile(newPos, TileRegistry.SOLD_TILE.get(), 1);
                    var TE = getLevel().getTileEntity(newPos);
                    if (TE instanceof SolidWallTileEntity STE) {
                        STE.ticks = this.ticks;
                        STE.useCobble = this.useCobble;
                        STE.mode = mode;
                        STE.markDirty();
                    }
                } else {
                    mode = 0;
                }
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
