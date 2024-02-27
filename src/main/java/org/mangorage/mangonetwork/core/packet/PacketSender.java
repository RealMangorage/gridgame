package org.mangorage.mangonetwork.core.packet;

import org.mangorage.mangonetwork.core.Side;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public final class PacketSender {
    private final LinkedList<Packet> queuedPackets = new LinkedList<>();
    private final Side side;

    public PacketSender(Side side) {
        this.side = side;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                PacketSender.this.sendNextPacket();
            }
        }, 0, 20);
    }

    public Side getSide() {
        return side;
    }

    public void send(Packet packet) {
        queuedPackets.add(packet);
    }

    private LinkedList<Packet> getQueuedPackets() {
        return queuedPackets;
    }


    public void sendNextPacket() {
        try {
            var packets = getQueuedPackets();
            if (packets.isEmpty()) return;
            var packet = packets.poll();
            var channel = packet.channel();

            if (channel != null && channel.isActive()) {
                channel.writeAndFlush(packet.packet()).sync();
                System.out.println("Sending Packet %s to %s with size of %s bytes".formatted(packet.packetName(), getSide(), packet.packet().content().readableBytes()));
            }
        } catch (Exception e) {
            System.out.println("Packet failed to send properly...");
            throw new RuntimeException(e);
        }
    }
}
