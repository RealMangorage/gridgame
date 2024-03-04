package org.mangorage.mangonetwork.core.packet;

import io.netty.channel.Channel;
import org.mangorage.mangonetwork.core.Side;

import java.net.InetSocketAddress;

public record Context(InetSocketAddress socketAddress, Channel channel, Side from) {}
