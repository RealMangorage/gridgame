package org.mangorage.gridgame.api;

import org.mangorage.gridgame.registry.Tiles;

public record TilePos(int x, int y) {
    public static long pack(int x, int y) {
        // Pack the two integers into a single long
        // Shift the first integer to the left by 32 bits and then combine it with the second integer using bitwise OR
        return ((long) x << 32) | (y & 0xFFFFFFFFL);
    }

    public static TilePos unPack(long packedLong) {
        return new TilePos(unpackX(packedLong), unpackY(packedLong));
    }

    private static int unpackX(long packedLong) {
        // Extract the first integer from the packed long
        // Shift the packed long to the right by 32 bits
        return (int) (packedLong >> 32);
    }

    private static int unpackY(long packedLong) {
        // Extract the second integer from the packed long
        // Perform a bitwise AND operation with 0xFFFFFFFF to get the last 32 bits
        return (int) (packedLong & 0xFFFFFFFFL);
    }
}
