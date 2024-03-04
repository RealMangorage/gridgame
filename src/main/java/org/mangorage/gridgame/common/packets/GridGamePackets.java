package org.mangorage.gridgame.common.packets;

import org.mangorage.gridgame.common.packets.serverbound.C2SPlayerJoinPacket;
import org.mangorage.gridgame.common.packets.serverbound.C2SPlayerMovePacket;
import org.mangorage.gridgame.common.packets.clientbound.S2CPlayerMovePacket;
import org.mangorage.gridgame.common.packets.clientbound.S2CTileEntityUpdatePacket;
import org.mangorage.gridgame.common.packets.clientbound.S2CTileUpdatePacket;
import org.mangorage.gridgame.common.packets.clientbound.S2CWorldLoadPacket;
import org.mangorage.mangonetwork.core.packet.PacketHandler;

public class GridGamePackets {
    public static int ID = 1000;

    public static final PacketHandler<C2SPlayerJoinPacket> PLAYER_JOIN_PACKET = PacketHandler.create(
            C2SPlayerJoinPacket.class,
            ID++
    );

    public static final PacketHandler<S2CWorldLoadPacket> WORLD_LOAD_PACKET = PacketHandler.create(
            S2CWorldLoadPacket.class,
            ID++
    );

    public static final PacketHandler<S2CTileUpdatePacket> TILE_UPDATE_PACKET = PacketHandler.create(
            S2CTileUpdatePacket.class,
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
