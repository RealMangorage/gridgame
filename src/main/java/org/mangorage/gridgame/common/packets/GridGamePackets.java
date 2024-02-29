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

    public static final PacketHandler<C2SPlayerMovePacket> PLAYER_MOVE_PACKET_C2S = PacketHandler.create(
            C2SPlayerMovePacket.class,
            ID++,
            C2SPlayerMovePacket::encode,
            C2SPlayerMovePacket::new,
            C2SPlayerMovePacket::handle
    );

    public static final PacketHandler<S2CPlayerMovePacket> PLAYER_MOVE_PACKET_S2C = PacketHandler.create(
            S2CPlayerMovePacket.class,
            ID++,
            S2CPlayerMovePacket::encode,
            S2CPlayerMovePacket::new,
            S2CPlayerMovePacket::handle
    );


    public static void init() {
    }
}
