package org.mangorage.mangonetwork.core.packet;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;
import org.mangorage.mangonetwork.core.Side;
import org.mangorage.mangonetwork.core.SimpleByteBuf;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PacketHandler<T> {
    private static final int PACKET_SIZE = 65536;
    private static final HashMap<Integer, PacketHandler<?>> PACKETS = new HashMap<>();
    private static final HashMap<Class<?>, PacketHandler<?>> PACKETS_REVERSE = new HashMap<>();

    private static final BiConsumer<EmptyPacket, SimpleByteBuf> EMPTY_ENCODER = (p, b) -> {};
    private static final Function<SimpleByteBuf, EmptyPacket> EMPTY_DECODER = (s) -> EmptyPacket.INSTANCE;
    private static final IHandler<EmptyPacket> EMPTY_HANDLER = (packet, origin, side) -> {};


    public static PacketResponse<?> receivePacket(DatagramPacket datagramPacket) {
        SimpleByteBuf headerBuffer = new SimpleByteBuf(datagramPacket.content());
        int totalSize = headerBuffer.readableBytes();
        int packetId = headerBuffer.readInt();
        Side from = headerBuffer.readEnum(Side.class);

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
                    from,
                    datagramPacket.sender()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void handle(T packet, int packetId, InetSocketAddress origin, Side side) {
        try {
            if (PACKETS.containsKey(packetId)) {
                ((PacketHandler<T>) PACKETS.get(packetId)).getHandler().handle(packet, origin, side);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> PacketHandler<T> create(Class<T> type, int ID, BiConsumer<T, SimpleByteBuf> encoder, Function<SimpleByteBuf, T> decoder, IHandler<T> handler) {
        return create(type, type.getName(), ID, encoder, decoder, handler);
    }

    public static PacketHandler<EmptyPacket> createEmpty(String nameID, int ID, IHandler<EmptyPacket> handler) {
        return create(EmptyPacket.class, nameID, ID, EMPTY_ENCODER, EMPTY_DECODER, handler);
    }

    public static PacketHandler<EmptyPacket> createEmptyNoHandler(String nameID, int ID) {
        return create(EmptyPacket.class, nameID, ID, EMPTY_ENCODER, EMPTY_DECODER, EMPTY_HANDLER);
    }

    public static <T> PacketHandler<T> create(Class<T> type, String nameID, int ID, BiConsumer<T, SimpleByteBuf> encoder, Function<SimpleByteBuf, T> decoder, IHandler<T> handler) {
        System.out.println("Created Packet Handler for: %s (Packet ID: %s)".formatted(type.getName(), ID));
        PacketHandler<T> packetHandler = new PacketHandler<>(type, nameID, ID, encoder, decoder, handler);
        PACKETS.put(ID, packetHandler);
        PACKETS_REVERSE.put(type, packetHandler);
        return packetHandler;
    }

    public static <T extends IPacket> PacketHandler<T> get(Class<T> packet) {
        return (PacketHandler<T>) PACKETS_REVERSE.get(packet);
    }


    public interface IHandler<T> {
        void handle(T packet, InetSocketAddress origin, Side side);
    }

    private final Class<T> clazz;
    private final String nameID;
    private final int ID;
    private final BiConsumer<T, SimpleByteBuf> encoder;
    private final Function<SimpleByteBuf, T> decoder;
    private final IHandler<T> handler;

    private PacketHandler(Class<T> type, String nameID,  int ID, BiConsumer<T, SimpleByteBuf> encoder, Function<SimpleByteBuf, T> decoder, IHandler<T> handler) {
        this.clazz = type;
        this.nameID = nameID;
        this.ID = ID;
        this.encoder = encoder;
        this.decoder = decoder;
        this.handler = handler;
    }

    public void send(T packet, PacketSender sender, InetSocketAddress sendTo, Channel channel) {
        SimpleByteBuf headerBuffer = new SimpleByteBuf(Unpooled.buffer(8));
        SimpleByteBuf packetBuffer = new SimpleByteBuf(Unpooled.buffer(8));
        encoder.accept(packet, packetBuffer);

        headerBuffer.writeInt(ID); // ID
        headerBuffer.writeEnum(sender.getSenderSide()); // Side
        headerBuffer.writeByteArray(packetBuffer.array());

        byte[] data = headerBuffer.array();
        if (data.length > PACKET_SIZE) {
            System.err.println("Unable to send packet %s exceeds packet size of %s, size of packet %s".formatted(packet.getClass().getName(), PACKET_SIZE, data.length));
            return;
        }

        try {
            DatagramPacket datagramPacket = new DatagramPacket(headerBuffer, sendTo);
            channel.writeAndFlush(datagramPacket).sync();

            System.out.println("Sent Packet %s to %s with size of %s bytes".formatted(packet.getClass().getName(), sender.getSenderSide().getOpposite(), datagramPacket.content().readableBytes()));
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
