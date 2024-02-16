package org.mangorage.gridgame.registry;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.mangorage.gridgame.api.grid.ITile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class TileRegistry {
    private final static TileRegistry TILE_REGISTRY = new TileRegistry();

    public static TileRegistry getInstance() {
        return TILE_REGISTRY;
    }

    private final Map<String, ITile> TILES = new HashMap<>();
    private final Map<ITile, String> TILES_ID_LOOKUP = new HashMap<>();
    private Map<String, Short> TILE_LOOKUP;

    public static Map<Short, String> getTileLookupFromTag(ListTag<CompoundTag> lookup) {
        Map<Short, String> lookupMap = new HashMap<>();
        lookup.forEach(tag -> {
            lookupMap.put(
                    tag.getShort("idShort"),
                    tag.getString("id")
            );
        });
        return lookupMap;
    }

    public <T extends ITile> T register(String id, T tile) {
        TILES.put(id, tile);
        TILES_ID_LOOKUP.put(tile, id);
        return tile;
    }

    public ITile getTile(String id) {
        return TILES.get(id);
    }

    public String getID(ITile tile) {
        return TILES_ID_LOOKUP.get(tile);
    }

    public void createTileLookup() {
        if (TILE_LOOKUP != null) return;

        Map<String, Short> data = new HashMap<>();
        AtomicReference<Short> count = new AtomicReference<>((short) 0);

        TILES.forEach((k, v) -> {
            data.put(k, count.get());
            count.set((short) (count.get() + 1));
        });

        this.TILE_LOOKUP = data;
    }

    public Map<String, Short> getTileLookup() {
        return TILE_LOOKUP;
    }

}
