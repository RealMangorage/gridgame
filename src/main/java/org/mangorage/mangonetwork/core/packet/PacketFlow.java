package org.mangorage.mangonetwork.core.packet;

public enum PacketFlow {
    CLIENTBOUND,
    SERVERBOUND;

    public PacketFlow getOpposite() {
        return this == CLIENTBOUND ? SERVERBOUND : CLIENTBOUND;
    }
}
