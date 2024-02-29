package org.mangorage.gridgame.client;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public record WrappedSocketAddress(InetSocketAddress address) implements Comparable<WrappedSocketAddress> {

        @Override
        public int compareTo(WrappedSocketAddress o) {
            // Compare the addresses of the WrappedSocketAddresses
            return this.address().toString().compareTo(o.address().toString());
        }
    }
    public static void main(String[] args) {
        var a = new InetSocketAddress("localhost", 2);
        var b = new InetSocketAddress("localhost", 2);
        System.out.println(new WrappedSocketAddress(a).equals(new WrappedSocketAddress(b)));
        System.out.println(a.equals(b));

        Map<InetSocketAddress, Object> OBJECTS = new HashMap<>();
        OBJECTS.put(a, "LOL");
        System.out.println(OBJECTS.get(b));

    }
}
