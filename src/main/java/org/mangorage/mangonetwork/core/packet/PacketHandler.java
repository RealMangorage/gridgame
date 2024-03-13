package org.mangorage.mangonetwork.core.packet;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.mangorage.mangonetwork.core.DebugState;
import org.mangorage.mangonetwork.core.SimpleByteBuf;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class PacketHandler<T extends IPacket> {
    public interface IHandler<T> {
        void handle(T packet, Context context);
    }

    public static final int MAX_PACKET_SIZE = 65536;

    private static final Int2ObjectMap<PacketHandler<? extends IPacket>> PACKETS = new Int2ObjectArrayMap<>();
    private static final Map<Class<?>, PacketHandler<? extends IPacket>> PACKETS_REVERSE = new HashMap<>();

    public static void printDebug(PacketResponse<?> r) {
        DebugState.PRINT_RESPONSE.print(r.packetName(), r.packetFlow(), r.source());
    }

    private static <T extends IPacket> void printDebugSent(T packet, PacketFlow packetFlow, DatagramPacket datagramPacket) {
        DebugState.PRINT_SENT.print(packet.getClass().getName(), packetFlow, datagramPacket.content().readableBytes());
    }

    private static <T extends IPacket> void printDebugReceive(int packetID, DatagramPacket datagramPacket, int totalSize) {
        DebugState.PRINT_RECEIVE.print(packetID, datagramPacket.sender(), totalSize);
    }


    public static PacketResponse<?> receivePacket(DatagramPacket datagramPacket, PacketFlow packetFlow) {
        SimpleByteBuf headerBuffer = new SimpleByteBuf(datagramPacket.content());
        int totalSize = headerBuffer.readableBytes();
        int packetId = headerBuffer.readInt();

        if (packetId < 0 || !PACKETS.containsKey(packetId)) {
            System.out.println("Received Bad Packet (Packet ID/Type: %s %s) from %s....".formatted(packetId, PACKETS.containsKey(packetId) ? PACKETS.get(packetId).getNameID() : "Unknown", datagramPacket.sender()));
            return null;
        }

        SimpleByteBuf packetBuffer = new SimpleByteBuf(Unpooled.wrappedBuffer(headerBuffer.readByteArray()));

        var handler = PACKETS.get(packetId);
        if (handler.packetFlow != packetFlow) {
            System.out.println("Cannot Receive Packet %s due to it being recieved on wrong end, Recieved on %s instead of %s".formatted(handler.getClazz().getName(), packetFlow, handler.packetFlow));
            return null;
        }

        printDebugReceive(packetId, datagramPacket, totalSize);

        try {
            return new PacketResponse<>(
                    handler.getDecoder().apply(packetBuffer),
                    handler.getNameID(),
                    packetId,
                    packetFlow,
                    datagramPacket.sender()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends IPacket> void handle(T packet, int packetId, Context context) {
        try {
            if (PACKETS.containsKey(packetId)) {
                ((PacketHandler<T>) PACKETS.get(packetId)).getHandler().handle(packet, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T extends IPacket> PacketHandler<T> create(Class<T> type, int ID) {
        return create(
                type,
                ID,
                IPacket::encode,
                IPacket.create(type),
                IPacket::handle
        );
    }

    public static <T extends IPacket> PacketHandler<T> create(Class<T> type, int ID, BiConsumer<T, SimpleByteBuf> encoder, Function<SimpleByteBuf, T> decoder, IHandler<T> handler) {
        var annotation = type.getAnnotation(PacketDirection.class);
        if (annotation == null)
            throw new IllegalStateException("org.mangorage.mangonetwork.core.packet.PacketDirection is missing from %s".formatted(type));

        return create(type, type.getName(), ID, annotation.flow(), encoder, decoder, handler);
    }

    public static <T extends IPacket> PacketHandler<T> create(Class<T> type, int ID, PacketFlow packetFlow) {
        return create(
                type,
                ID,
                packetFlow,
                IPacket::encode,
                IPacket.create(type),
                IPacket::handle
        );
    }

    public static <T extends IPacket> PacketHandler<T> create(Class<T> type, int ID,  PacketFlow packetFlow, BiConsumer<T, SimpleByteBuf> encoder, Function<SimpleByteBuf, T> decoder, IHandler<T> handler) {
        return create(type, type.getName(), ID, packetFlow, encoder, decoder, handler);
    }

    public static <T extends IPacket> PacketHandler<T> create(Class<T> type, String nameID, int ID, PacketFlow packetFlow, BiConsumer<T, SimpleByteBuf> encoder, Function<SimpleByteBuf, T> decoder, IHandler<T> handler) {
        System.out.println("Created Packet Handler for: %s with PacketFlow of: %s (Packet ID: %s)".formatted(type.getName(), packetFlow, ID));
        PacketHandler<T> packetHandler = new PacketHandler<>(type, nameID, ID, packetFlow, encoder, decoder, handler);
        PACKETS.put(ID, packetHandler);
        PACKETS_REVERSE.put(type, packetHandler);
        return packetHandler;
    }

    public static PacketHandler<? extends IPacket> getId(int id) {
        return PACKETS.get(id);
    }

    public static PacketHandler<? extends IPacket> get(Class<? extends IPacket> packet) {
        return PACKETS_REVERSE.get(packet);
    }

    @SuppressWarnings("unchecked")
    public static PacketHandler<IPacket> cast(PacketHandler<? extends IPacket> packetHandler) {
        return (PacketHandler<IPacket>) packetHandler;
    }


    private final Class<T> clazz;
    private final String nameID;

    private final int ID;
    private final PacketFlow packetFlow;

    private final BiConsumer<T, SimpleByteBuf> encoder;
    private final Function<SimpleByteBuf, T> decoder;
    private final IHandler<T> handler;

    private PacketHandler(Class<T> type, String nameID, int ID, PacketFlow packetFlow, BiConsumer<T, SimpleByteBuf> encoder, Function<SimpleByteBuf, T> decoder, IHandler<T> handler) {
        this.clazz = type;
        this.nameID = nameID;
        this.ID = ID;
        this.packetFlow = packetFlow;
        this.encoder = encoder;
        this.decoder = decoder;
        this.handler = handler;
    }

    public void send(T packet, PacketFlow packetFlow, InetSocketAddress recipient, Channel channel) {
        if (this.packetFlow == packetFlow.getOpposite()) {
            System.out.println("Cannot send packet %s to %s, only goes to %s".formatted(packet.getClass(), packetFlow, this.packetFlow));
            return;
        }

        SimpleByteBuf headerBuffer = new SimpleByteBuf(Unpooled.buffer(8));
        SimpleByteBuf packetBuffer = new SimpleByteBuf(Unpooled.buffer(8));
        encoder.accept(packet, packetBuffer);

        headerBuffer.writeInt(ID); // Packet ID
        headerBuffer.writeByteArray(packetBuffer.array());

        if (headerBuffer.array().length > MAX_PACKET_SIZE) {
            System.err.println("Unable to send packet %s exceeds packet size of %s, size of packet %s".formatted(packet.getClass().getName(), MAX_PACKET_SIZE, headerBuffer.array().length));
            return;
        }

        try {
            DatagramPacket datagramPacket = new DatagramPacket(headerBuffer, recipient);
            channel.writeAndFlush(datagramPacket).sync();
            printDebugSent(packet, packetFlow, datagramPacket);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Function<SimpleByteBuf, T> getDecoder() {
        return decoder;
    }

    public IHandler<T> getHandler() {
        return handler;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public String getNameID() {
        return nameID;
    }
}
