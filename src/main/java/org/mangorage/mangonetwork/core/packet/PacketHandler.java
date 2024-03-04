package org.mangorage.mangonetwork.core.packet;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.mangorage.mangonetwork.core.LogicalSide;
import org.mangorage.mangonetwork.core.SimpleByteBuf;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class PacketHandler<T extends IPacket> {
    private final static ScheduledThreadPoolExecutor EXECUTOR = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(4);


    private static final int MAX_PACKET_SIZE = 65536;

    private static final Int2ObjectMap<PacketHandler<?>> PACKETS = new Int2ObjectArrayMap<>();
    private static final Map<Class<?>, PacketHandler<?>> PACKETS_REVERSE = new HashMap<>();


    public static PacketResponse<?> receivePacket(DatagramPacket datagramPacket, PacketFlow packetFlow) {
        SimpleByteBuf headerBuffer = new SimpleByteBuf(datagramPacket.content());
        int totalSize = headerBuffer.readableBytes();
        int packetId = headerBuffer.readInt();
        LogicalSide from = headerBuffer.readEnum(LogicalSide.class);

        System.out.println("%s %s".formatted(packetId, from));
        SimpleByteBuf packetBuffer = new SimpleByteBuf(Unpooled.wrappedBuffer(headerBuffer.readByteArray()));

        if (packetId < 0 || from == null || !PACKETS.containsKey(packetId)) {
            System.out.println("Received Bad Packet (Packet ID/Type: %s %s) from %s....".formatted(packetId, PACKETS.containsKey(packetId) ? PACKETS.get(packetId).getNameID() : "Unknown", datagramPacket.sender()));
            return null;
        }

        var handler = PACKETS.get(packetId);
        System.out.println("Packet ID %s from %s received with size: %s bytes".formatted(packetId, datagramPacket.sender(), totalSize));

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

    public static <T extends IPacket> PacketHandler<T> getId(int id) {
        return (PacketHandler<T>) PACKETS.get(id);
    }

    public static <T extends IPacket> PacketHandler<T> get(Class<T> packet) {
        return (PacketHandler<T>) PACKETS_REVERSE.get(packet);
    }

    public static void execute(Runnable runnable) {
        EXECUTOR.execute(runnable);
    }

    public static void schedule(Runnable runnable, int i, TimeUnit timeUnit) {
        EXECUTOR.schedule(runnable, i, timeUnit);
    }

    public interface IHandler<T> {
        void handle(T packet, Context context);
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

    public void send(T packet, PacketFlow packetFlow, InetSocketAddress sendTo, Channel channel) {
        if (this.packetFlow == packetFlow.getOpposite()) {
            System.out.println("Cannot send packet %s to %s, only goes to %s".formatted(packet.getClass(), packetFlow, this.packetFlow));
            return;
        }

        SimpleByteBuf headerBuffer = new SimpleByteBuf(Unpooled.buffer(8));
        SimpleByteBuf packetBuffer = new SimpleByteBuf(Unpooled.buffer(8));
        encoder.accept(packet, packetBuffer);

        headerBuffer.writeInt(ID); // ID
        headerBuffer.writeEnum(packetFlow); // Side
        headerBuffer.writeByteArray(packetBuffer.array());

        byte[] data = headerBuffer.array();
        if (data.length > MAX_PACKET_SIZE) {
            System.err.println("Unable to send packet %s exceeds packet size of %s, size of packet %s".formatted(packet.getClass().getName(), MAX_PACKET_SIZE, data.length));
            return;
        }

        try {
            DatagramPacket datagramPacket = new DatagramPacket(headerBuffer, sendTo);
            channel.writeAndFlush(datagramPacket).sync();

            System.out.println("Sent Packet %s to %s with size of %s bytes".formatted(packet.getClass().getName(), packetFlow, datagramPacket.content().readableBytes()));
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
