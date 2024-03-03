package org.mangorage.gridgame.common.packets;

import net.querz.nbt.tag.CompoundTag;
import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.gridgame.common.world.TileEntity;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.mangonetwork.core.Side;
import org.mangorage.mangonetwork.core.SimpleByteBuf;
import org.mangorage.mangonetwork.core.packet.IPacket;

import java.net.InetSocketAddress;

public class S2CTileEntityUpdatePacket implements IPacket {
    private final TilePos tilePos;
    private final CompoundTag tag;

    public S2CTileEntityUpdatePacket(TileEntity tileEntity) {
        this.tilePos = tileEntity.getPos();
        this.tag = tileEntity.getUpdateTag();
    }

    public S2CTileEntityUpdatePacket(SimpleByteBuf byteBuf) {
        this.tilePos = byteBuf.readTilePos();
        this.tag = byteBuf.readNBT();
    }

    @Override
    public void encode(SimpleByteBuf buffer) {
        buffer.writeTilePos(tilePos);
        buffer.writeNBT(tag);
    }

    @Override
    public void handle(InetSocketAddress originAddress, Side fromSide) {
        if (fromSide == Side.SERVER) {
            GridGameClient.getInstance().getLevel().getTileEntity(tilePos).loadUpdateTag(tag);
        }
    }
}
