package org.mangorage.gridgame.registry;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ByteArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import org.mangorage.gridgame.api.grid.ITile;


public class TileRegistry {
    private final static TileRegistry TILE_REGISTRY = new TileRegistry();

    public static TileRegistry getInstance() {
        return TILE_REGISTRY;
    }

    private final Byte2ObjectMap<ITile> TILES = new Byte2ObjectArrayMap<>();
    private final Object2ByteMap<ITile> TILES_REVERSED = new Object2ByteArrayMap<>();

    public <T extends ITile> T register(int idInt, T tile) {
        var id = (byte) idInt;
        TILES.put(id, tile);
        TILES_REVERSED.put(tile, id);
        return tile;
    }

    public ITile getTile(byte id) {
        return TILES.get(id);
    }

    public byte getID(ITile tile) {
        return TILES_REVERSED.getByte(tile);
    }
}
