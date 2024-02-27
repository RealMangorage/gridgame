package org.mangorage.gridgame.server;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.mangorage.gridgame.common.packets.TileUpdatePacket;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.mangonetwork.Packets;
import org.mangorage.mangonetwork.core.Connection;
import org.mangorage.mangonetwork.core.packet.PacketHandler;
import org.mangorage.mangonetwork.core.packet.PacketResponse;
import org.mangorage.mangonetwork.core.Scheduler;
import org.mangorage.mangonetwork.core.Side;
import org.mangorage.mangonetwork.core.packet.PacketSender;
import org.mangorage.mangonetwork.packets.MessagePacket;

public class Server extends Thread {
    private static Server instance;

    public static void main(String[] args)  {
        Packets.init();
        instance = new Server(25565);
        instance.start();
    }

    private final int port;
    private final PacketSender packetSender = new PacketSender(Side.SERVER);

    private Server(int port) {
        System.out.println("Starting Server Version 1.0");
        this.port = port;

        GridGameServer.init();
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

                            ch.pipeline().addLast(new SimpleChannelInboundHandler<DatagramPacket>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
                                    PacketResponse<?> response = PacketHandler.receivePacket(packet);
                                    if (response != null) {
                                        Scheduler.RUNNER.execute(() -> {
                                            PacketHandler.handle(response.packet(), response.packetId(), response.source(), response.sentFrom());

                                            System.out.printf("Received Packet: %s%n", response.packetName());
                                            System.out.printf("From Side: %s%n", response.sentFrom());
                                            System.out.printf("Source: %s%n", response.source());

                                            Connection connection = new Connection(() -> ch, response.source(), packetSender);
                                            connection.send(new MessagePacket("LOL FROM SERVER!"));

                                            GridGameServer.getInstance().addPlayer(connection);
                                        });
                                    }
                                }
                            });

                            System.out.println("Server Started");
                        }
                    });


            b.bind(port).sync().channel().closeFuture().await();
        } catch (Exception e) {

        } finally {
            group.shutdownGracefully();
        }
    }
}