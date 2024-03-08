package org.mangorage.gridgame.common.registry;

import org.mangorage.gridgame.common.Registries;
import org.mangorage.gridgame.common.core.registry.Holder;
import org.mangorage.gridgame.common.world.Tile;
import org.mangorage.gridgame.common.world.tiles.EmptyTile;
import org.mangorage.gridgame.common.world.tiles.PlayerTile;
import org.mangorage.gridgame.common.world.tiles.SolidTile;

public class TileRegistry {
    public static final Holder<EmptyTile> EMPTY_TILE = Registries.TILE_REGISTRY.register("empty", EmptyTile::new);
    public static final Holder<SolidTile> SOLD_TILE = Registries.TILE_REGISTRY.register("solid", SolidTile::new);
    public static final Holder<PlayerTile> PLAYER_TILE = Registries.TILE_REGISTRY.register("player", PlayerTile::new);
    public static final Holder<Tile> COBBLESTONE_TILE = Registries.TILE_REGISTRY.register("cobblestone", Tile::new);

    public static void init() {}
}
