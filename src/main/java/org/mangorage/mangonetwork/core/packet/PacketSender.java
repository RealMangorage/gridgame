package org.mangorage.mangonetwork.core.packet;

import org.mangorage.mangonetwork.core.Side;

public record PacketSender(Side fromSide) {
    public Side getSenderSide() {
        return fromSide;
    }
}
