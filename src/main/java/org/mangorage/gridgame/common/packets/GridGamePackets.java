package org.mangorage.gridgame.common.packets;

import org.mangorage.mangonetwork.core.packet.PacketHandler;

public class GridGamePackets {
    public static int ID = 1000;

    public static final PacketHandler<WorldLoadPacket> WORLD_LOAD_PACKET = PacketHandler.create(
            WorldLoadPacket.class,
            ID++,
            WorldLoadPacket::encode,
            WorldLoadPacket::new,
            WorldLoadPacket::handle
    );

    public static final PacketHandler<TileUpdatePacket> TILE_UPDATE_PACKET = PacketHandler.create(
            TileUpdatePacket.class,
            ID++,
            TileUpdatePacket::encode,
            TileUpdatePacket::new,
            TileUpdatePacket::handle
    );

    public static void init() {
    }
}
