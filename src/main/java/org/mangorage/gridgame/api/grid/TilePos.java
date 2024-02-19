package org.mangorage.gridgame.api.grid;

public record TilePos(int x, int y, int z) {
    public static long pack(int x, int y, int z) {
        // Pack the three integers into a single long
        // Shift x to the left by 42 bits, y by 21 bits, then combine with z using bitwise OR
        return ((long) x << 42) | ((long) y << 21) | (z & 0x1FFFFFL);
    }

    public static TilePos unPack(long packedLong) {
        return new TilePos(unpackX(packedLong), unpackY(packedLong), unpackZ(packedLong));
    }

    private static int unpackX(long packedLong) {
        // Extract the x coordinate from the packed long
        // Shift packedLong to the right by 42 bits
        return (int) (packedLong >> 42);
    }

    private static int unpackY(long packedLong) {
        // Extract the y coordinate from the packed long
        // Shift packedLong to the right by 21 bits, then perform bitwise AND with 0x1FFFFF
        return (int) ((packedLong >> 21) & 0x1FFFFF);
    }

    private static int unpackZ(long packedLong) {
        // Extract the z coordinate from the packed long
        // Perform bitwise AND operation with 0x1FFFFF to get the last 21 bits
        return (int) (packedLong & 0x1FFFFFL);
    }
}
