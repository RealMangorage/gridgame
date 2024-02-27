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
import org.mangorage.mangonetwork.Packets;
import org.mangorage.mangonetwork.core.Connection;
import org.mangorage.mangonetwork.core.packet.PacketHandler;
import org.mangorage.mangonetwork.core.packet.PacketResponse;
import org.mangorage.mangonetwork.core.Scheduler;
import org.mangorage.mangonetwork.core.Side;
import org.mangorage.mangonetwork.packets.MessagePacket;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class Client {
    private static Client instance;

    public static void main(String[] args) throws SocketException {
        Packets.init();
        // 23.26.60.28:14126
        instance = new Client("localhost:25565");
    }

    private final InetSocketAddress server;
    private final AtomicReference<Channel> channel = new AtomicReference<>();
    private final Connection connection;


    public Client(String IP) {
        System.out.println("Starting Client Version 1.0 to IP: %s".formatted(IP));
        String[] ipArr = IP.split(":");

        this.server = new InetSocketAddress(ipArr[0], Integer.parseInt(ipArr[1]));
        this.connection = new Connection(channel::get, server, Side.CLIENT);

        CompletableFuture.runAsync(() -> {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioDatagramChannel.class)
                        .handler(new ChannelInitializer<>() {
                            @Override
                            public void initChannel(Channel ch) {
                                ch.pipeline().addLast(new SimpleChannelInboundHandler<DatagramPacket>() {

                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                        Client.this.channel.set(ctx.channel());
                                    }

                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
                                        PacketResponse<?> response = PacketHandler.receivePacket(packet);
                                        if (response != null) {
                                            Scheduler.RUNNER.execute(() -> {
                                                PacketHandler.handle(response.packet(), response.packetId(), response.source(), response.sentFrom());

                                                Client.this.channel.set(ctx.channel());

                                                System.out.printf("Received Packet: %s%n", response.packetName());
                                                System.out.printf("From Side: %s%n", response.sentFrom());
                                                System.out.printf("Source: %s%n", response.source());
                                            });
                                        }
                                    }
                                });

                                Client.this.channel.set(ch);

                                connection.send(new MessagePacket("WOOP?"));


                                System.out.println("Client Started...");
                            }
                        });

                b.bind(0).sync().channel().closeFuture().await();
            } catch (Exception ignored) {

            } finally {
                group.shutdownGracefully();
            }
        });

    }
}
