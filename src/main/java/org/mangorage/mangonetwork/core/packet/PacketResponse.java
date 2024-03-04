package org.mangorage.mangonetwork.core.packet;

import java.net.InetSocketAddress;

public record PacketResponse<T extends IPacket>(T packet, String packetName, int packetId, PacketFlow packetFlow, InetSocketAddress source){}
