package org.mangorage.mangonetwork.core.packet;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

public record Context(InetSocketAddress socketAddress, Channel channel, PacketFlow packetFlow) {}
