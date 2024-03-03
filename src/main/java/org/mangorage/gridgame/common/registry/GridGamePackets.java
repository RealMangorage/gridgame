package org.mangorage.gridgame.common.registry;

import org.mangorage.gridgame.common.packets.C2SPlayerMovePacket;
import org.mangorage.gridgame.common.packets.S2CPlayerMovePacket;
import org.mangorage.gridgame.common.packets.S2CTileEntityUpdatePacket;
import org.mangorage.gridgame.common.packets.TileUpdatePacket;
import org.mangorage.gridgame.common.packets.WorldLoadPacket;
import org.mangorage.mangonetwork.core.packet.PacketHandler;

public class GridGamePackets {
    public static int ID = 1000;

    public static final PacketHandler<WorldLoadPacket> WORLD_LOAD_PACKET = PacketHandler.create(
            WorldLoadPacket.class,
            ID++
    );

    public static final PacketHandler<TileUpdatePacket> TILE_UPDATE_PACKET = PacketHandler.create(
            TileUpdatePacket.class,
            ID++
    );

    public static final PacketHandler<C2SPlayerMovePacket> PLAYER_MOVE_PACKET_C2S = PacketHandler.create(
            C2SPlayerMovePacket.class,
            ID++
    );

    public static final PacketHandler<S2CPlayerMovePacket> PLAYER_MOVE_PACKET_S2C = PacketHandler.create(
            S2CPlayerMovePacket.class,
            ID++
    );

    public static final PacketHandler<S2CTileEntityUpdatePacket> TILE_ENTITY_UPDATE_PACKET = PacketHandler.create(
            S2CTileEntityUpdatePacket.class,
            ID++
    );


    public static void init() {
    }
}
