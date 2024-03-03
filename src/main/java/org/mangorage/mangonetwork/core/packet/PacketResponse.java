package org.mangorage.mangonetwork.core.packet;
import org.mangorage.mangonetwork.core.Side;

import java.net.InetSocketAddress;

public record PacketResponse<T extends IPacket>(T packet, String packetName, int packetId, Side sentFrom, InetSocketAddress source){}
