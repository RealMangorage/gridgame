package org.mangorage.example.tiles;

import org.mangorage.gridgame.core.grid.tiles.Tile;
import org.mangorage.gridgame.core.registry.Holder;
import org.mangorage.gridgame.core.registry.Registries;
import org.mangorage.gridgame.core.registry.Registry;

public class TileRegistry {
    private static final Registry<Tile> TILE_REGISTRY = Registries.TILE_REGISTRY;

    public static final Holder<ExampleTile> EXAMPLE_TILE = TILE_REGISTRY.register("example", ExampleTile::new);

    public static void init() {
    }
}
