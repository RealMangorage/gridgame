package org.mangorage.mangonetwork.core;

public class DebugState {
    public static final DebugOption PRINT_RECEIVE = new DebugOption(o -> {
        System.out.println("""
                ----------------------------------------------------
                Packet Receive Info:
                Packet ID %s from %s received with size: %s bytes
                ----------------------------------------------------
                """.formatted(o));
    });
    public static final DebugOption PRINT_SENT = new DebugOption(o -> {
        System.out.println("""
                ----------------------------------------------------
                Packet Send Info:
                Sent Packet %s to %s with size of %s bytes
                ----------------------------------------------------
                """.formatted(o));
    });
    public static final DebugOption PRINT_RESPONSE = new DebugOption(o -> {
        System.out.println("""
                ----------------------------------------------------
                Packet Response Info:
                Received Packet: %s
                PacketFlow: %s
                Source: %s
                ----------------------------------------------------
                """.formatted(o));
    });
}
