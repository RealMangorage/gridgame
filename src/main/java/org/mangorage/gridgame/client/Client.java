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
import org.mangorage.gridgame.common.packets.serverbound.C2SPlayerJoinPacket;
import org.mangorage.mangonetwork.core.connection.Connection;
import org.mangorage.mangonetwork.core.packet.Context;
import org.mangorage.mangonetwork.core.packet.PacketFlow;
import org.mangorage.mangonetwork.core.packet.PacketHandler;
import org.mangorage.mangonetwork.core.packet.PacketResponse;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void init() {
        init("localhost:25565", "MangoRage");
    }

    public static void init(String server, String username) {
        new Client(server, username);
    }

    private final InetSocketAddress server;
    private final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor((r) -> new Thread(r, "Client-Packet-Handler"));
    private final ScheduledExecutorService SERVICE_NETWORK = Executors.newSingleThreadScheduledExecutor((r) -> new Thread(r, "Client-Network-Handler"));
    private Connection connection;

    public Client(String IP, String username) {
        System.out.println("Starting Client Version 1.0 to IP: %s".formatted(IP));
        String[] ipArr = IP.split(":");

        this.server = new InetSocketAddress(ipArr[0], Integer.parseInt(ipArr[1]));

        CompletableFuture.runAsync(() -> {
            ThreadFactory threadFactory = (r) -> new Thread(r, "Client-Netty");
            EventLoopGroup group = new NioEventLoopGroup(threadFactory);

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
                                        PacketResponse<?> response = PacketHandler.receivePacket(packet, PacketFlow.CLIENTBOUND);

                                        if (response != null) {
                                            SERVICE.schedule(() -> {
                                                PacketHandler.handle(response.packet(), response.packetId(), new Context(connection, ctx.channel(), response.packetFlow()));

                                                System.out.printf("Received Packet: %s%n", response.packetName());
                                                System.out.printf("PacketFlow: %s%n", response.packetFlow());
                                                System.out.printf("Source: %s%n", response.source());
                                            }, 10, TimeUnit.MILLISECONDS);
                                        }
                                    }
                                });


                                System.out.println("Client Started...");
                            }
                        });

                var chl = b.connect().sync().channel();

                this.connection = new Connection(chl, server, PacketFlow.SERVERBOUND);

                GridGameClient.init(connection, username);

                connection.send(new C2SPlayerJoinPacket(username));
                chl.closeFuture().await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        }, SERVICE_NETWORK);

    }
}
