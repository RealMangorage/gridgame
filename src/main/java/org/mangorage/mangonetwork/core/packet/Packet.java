package org.mangorage.mangonetwork.core.packet;

import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;

public record Packet(DatagramPacket packet, String packetName, Channel channel) {
}
