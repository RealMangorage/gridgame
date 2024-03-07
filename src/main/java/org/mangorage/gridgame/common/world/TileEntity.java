package org.mangorage.gridgame.common.world;

import net.querz.nbt.tag.CompoundTag;
import org.mangorage.gridgame.common.packets.clientbound.S2CTileEntityUpdatePacket;
import org.mangorage.gridgame.server.GridGameServer;
import org.mangorage.mangonetwork.core.LogicalSide;

public abstract class TileEntity {
    private final Level level;
    private final TilePos pos;
    private final LogicalSide logicalSide;
    private boolean dirty = false;

    public TileEntity(Level level, TilePos pos, LogicalSide logicalSide) {
        this.level = level;
        this.pos = pos;
        this.logicalSide = logicalSide;
    }

    public Level getLevel() {
        return level;
    }

    public TilePos getPos() {
        return pos;
    }

    public LogicalSide getSide() {
        return logicalSide;
    }

    public boolean isClientSide() {
        return getSide() == LogicalSide.CLIENT;
    }

    public void preTick() {
        if (logicalSide == LogicalSide.SERVER) {
            if (dirty) {
                var packet = getUpdatePacket();
                if (packet != null)
                    GridGameServer.getInstance().getPipedConnection().send(packet);
                dirty = false;
            }
        }

        tick();
    }

    public void tick() {

    }

    public void markDirty() {
        dirty = true;
    }

    public S2CTileEntityUpdatePacket getUpdatePacket() {
        return null;
    }

    public CompoundTag getUpdateTag() {
        return save();
    }

    public void loadUpdateTag(CompoundTag tag) {
        load(tag);
    }

    public void load(CompoundTag tag) {

    }

    public CompoundTag save() {
        return new CompoundTag();
    }
}
