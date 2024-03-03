package org.mangorage.mangonetwork.core.packet;

import org.mangorage.mangonetwork.core.Side;
import org.mangorage.mangonetwork.core.SimpleByteBuf;

import java.net.InetSocketAddress;

public sealed abstract class EmptyPacket implements IPacket permits EmptyPacket.MainEmptyPacket {
    public static final EmptyPacket INSTANCE = new MainEmptyPacket();
    private EmptyPacket() {}

    static non-sealed class MainEmptyPacket extends EmptyPacket {
        @Override
        public void encode(SimpleByteBuf buffer) {

        }

        @Override
        public void handle(InetSocketAddress originAddress, Side fromSide) {

        }
    }
}
