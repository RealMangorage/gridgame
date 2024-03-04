package org.mangorage.gridgame.common.world;

import net.querz.nbt.tag.CompoundTag;
import org.mangorage.gridgame.common.packets.S2CTileEntityUpdatePacket;
import org.mangorage.gridgame.server.GridGameServer;
import org.mangorage.mangonetwork.core.Side;

public abstract class TileEntity {
    private final Level level;
    private final TilePos pos;
    private final Side side;
    private boolean dirty = false;

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

    public void preTick() {
        if (side == Side.SERVER) {
            if (dirty) {
                var packet = getUpdatePacket();
                if (packet != null)
                    GridGameServer.getInstance().getPipedConnection().send(packet);
                dirty = false;
            }
        }

        tick();
    }

    public void tick() {}

    public void markDirty() {
        dirty = true;
    }

    public CompoundTag getUpdateTag() {
        return new CompoundTag();
    }

    public S2CTileEntityUpdatePacket getUpdatePacket() {
        return null;
    }

    public void loadUpdateTag(CompoundTag tag) {}

    public void load(CompoundTag tag) {}
    public CompoundTag save() {return new CompoundTag();}
}
