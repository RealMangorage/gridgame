package org.mangorage.gridgame.common.packets.clientbound;

import net.querz.nbt.tag.CompoundTag;
import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.gridgame.common.world.TileEntity;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.mangonetwork.core.SimpleByteBuf;
import org.mangorage.mangonetwork.core.packet.Context;
import org.mangorage.mangonetwork.core.packet.IPacket;
import org.mangorage.mangonetwork.core.packet.PacketDirection;
import org.mangorage.mangonetwork.core.packet.PacketFlow;

@PacketDirection(flow = PacketFlow.CLIENTBOUND)
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
    public void handle(Context ctx) {
        System.out.println(tag);
        var TE = GridGameClient.getInstance().getLevel().getTileEntity(tilePos);
        if (TE != null)
            TE.loadUpdateTag(tag);
    }
}
