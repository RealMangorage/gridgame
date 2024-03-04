package org.mangorage.mangonetwork.core.packet;

import org.mangorage.mangonetwork.core.SimpleByteBuf;
import java.util.function.Function;

public interface IPacket {
    static <T extends IPacket> Function<SimpleByteBuf,T> create(Class<T> packetClass) {
        return (buf) -> {
            try {
                return packetClass.getConstructor(SimpleByteBuf.class).newInstance(buf);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalStateException("Attempted to get constructor for %s, does not exist...".formatted(packetClass));
            }
        };
    }

    void encode(SimpleByteBuf buffer);

    // fromSide -> The side that sent the packet, so if we get packets from Server, we are on client
    // if we get packets from client, we are on server...
    void handle(Context ctx);
}
