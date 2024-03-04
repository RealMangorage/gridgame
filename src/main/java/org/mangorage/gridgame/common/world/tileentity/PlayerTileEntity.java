package org.mangorage.gridgame.common.world.tileentity;

import net.querz.nbt.tag.CompoundTag;
import org.mangorage.gridgame.common.packets.S2CTileEntityUpdatePacket;
import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.TileEntity;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.mangonetwork.core.Side;

public class PlayerTileEntity extends TileEntity {

    private String username;

    public PlayerTileEntity(Level level, TilePos pos, Side side) {
        super(level, pos, side);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        markDirty();
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("username", username);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.username = tag.getString("username");
    }

    @Override
    public void loadUpdateTag(CompoundTag tag) {
        this.username = tag.getString("username");
    }

    @Override
    public CompoundTag getUpdateTag() {
        return save();
    }

    @Override
    public S2CTileEntityUpdatePacket getUpdatePacket() {
        return new S2CTileEntityUpdatePacket(this);
    }
}
