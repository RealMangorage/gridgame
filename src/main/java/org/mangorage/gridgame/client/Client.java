package org.mangorage.gridgame.client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.mangorage.gridgame.common.packets.C2SPlayerJoinPacket;
import org.mangorage.mangonetwork.core.connection.Connection;
import org.mangorage.mangonetwork.core.packet.Context;
import org.mangorage.mangonetwork.core.packet.PacketHandler;
import org.mangorage.mangonetwork.core.packet.PacketResponse;
import org.mangorage.mangonetwork.core.Scheduler;
import org.mangorage.mangonetwork.core.Side;
import org.mangorage.mangonetwork.core.packet.PacketSender;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class Client {

    public static void init() {
        init("localhost:25565", "MangoRage");
    }

    public static void init(String server, String username) {
        new Client(server, username);
    }

    private final InetSocketAddress server;
    private final AtomicReference<Channel> channel = new AtomicReference<>();


    public Client(String IP, String username) {
        System.out.println("Starting Client Version 1.0 to IP: %s".formatted(IP));
        String[] ipArr = IP.split(":");

        this.server = new InetSocketAddress(ipArr[0], Integer.parseInt(ipArr[1]));

        CompletableFuture.runAsync(() -> {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioDatagramChannel.class)
                        .remoteAddress(server)
                        .handler(new ChannelInitializer<>() {
                            @Override
                            public void initChannel(Channel ch) {
                                ch.pipeline().addLast(new SimpleChannelInboundHandler<DatagramPacket>() {

                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
                                        PacketResponse<?> response = PacketHandler.receivePacket(packet);
                                        if (response != null) {
                                            Scheduler.RUNNER.execute(() -> {
                                                PacketHandler.handle(response.packet(), response.packetId(), new Context(response.source(), ctx.channel(), response.sentFrom()));

                                                System.out.printf("Received Packet: %s%n", response.packetName());
                                                System.out.printf("From Side: %s%n", response.sentFrom());
                                                System.out.printf("Source: %s%n", response.source());
                                            });
                                        }
                                    }
                                });


                                System.out.println("Client Started...");
                            }


                        });

                var chl = b.connect().sync().channel();

                Connection connection = new Connection(chl, server, new PacketSender(Side.CLIENT));
                GridGameClient.init(connection);

                connection.send(new C2SPlayerJoinPacket(username));
                chl.closeFuture().await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        });

    }
}
