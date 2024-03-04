package org.mangorage.gridgame.server;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import org.mangorage.mangonetwork.core.connection.IPipedConnection;
import org.mangorage.mangonetwork.core.connection.PipedConnection;
import org.mangorage.mangonetwork.core.packet.Context;
import org.mangorage.mangonetwork.core.packet.PacketFlow;
import org.mangorage.mangonetwork.core.packet.PacketHandler;
import org.mangorage.mangonetwork.core.packet.PacketResponse;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Server extends Thread {
    private static Server instance;

    public static void init()  {
        instance = new Server(25565);
        instance.start();
    }

    private final int port;
    private final IPipedConnection pipedConnection = new PipedConnection();

    private Server(int port) {
        System.out.println("Starting Server Version 1.0");
        this.port = port;

        GridGameServer.init(pipedConnection);
    }

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();

            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<>() {
                        @Override
                        public void initChannel(final Channel ch) {

                            ch.pipeline().addLast(new MessageToMessageDecoder<DatagramPacket>() {
                                @Override
                                protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket msg, List<Object> out) {
                                    ByteBuf content = msg.content();
                                    if (content.readableBytes() >= PacketHandler.MAX_PACKET_SIZE) {
                                        System.out.println("Dropped packet: Packet size exceeds the allowed limit");
                                        return;
                                    }
                                    out.add(msg.retain());
                                }
                            });

                            ch.pipeline().addLast(new SimpleChannelInboundHandler<DatagramPacket>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
                                    PacketResponse<?> response = PacketHandler.receivePacket(packet, PacketFlow.SERVERBOUND);
                                    if (response != null) {
                                        PacketHandler.schedule(() -> {
                                            PacketHandler.handle(response.packet(), response.packetId(), new Context(response.source(), ch, response.packetFlow()));

                                            System.out.printf("Received Packet: %s%n", response.packetName());
                                            System.out.printf("PacketFlow: %s%n", response.packetFlow());
                                            System.out.printf("Source: %s%n", response.source());
                                        }, 10, TimeUnit.MILLISECONDS);
                                    }
                                }
                            });

                            System.out.println("Server Started");
                        }
                    });


            b.bind(port).sync().channel().closeFuture().await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
