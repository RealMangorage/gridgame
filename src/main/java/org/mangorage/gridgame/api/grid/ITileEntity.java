package org.mangorage.gridgame.api.grid;

import net.querz.nbt.tag.CompoundTag;

public interface ITileEntity {
    void tick();
    void save(CompoundTag tag);
    void load(CompoundTag tag);
}
