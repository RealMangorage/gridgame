package org.mangorage.gridgame.registry;

import org.mangorage.gridgame.core.grid.tiles.Tile;
import org.mangorage.gridgame.game.tiles.EmptyTile;
import org.mangorage.gridgame.game.tiles.PlayerTile;
import org.mangorage.gridgame.game.tiles.UnSolidWallTile;
import org.mangorage.gridgame.game.tiles.WallTile;
import org.mangorage.gridgame.core.registry.Holder;
import org.mangorage.gridgame.core.registry.Registries;
import org.mangorage.gridgame.core.registry.Registry;

public class Tiles {
    private static final Registry<Tile> TILE_REGISTRY = Registries.TILE_REGISTRY;


    public static final Holder<EmptyTile> EMPTY_TILE = TILE_REGISTRY.register("air", EmptyTile::new);
    public static final Holder<PlayerTile> PLAYER_TILE = TILE_REGISTRY.register("player", PlayerTile::new);
    public static final Holder<WallTile> WALL_TILE = TILE_REGISTRY.register("wall", WallTile::new);
    public static final Holder<UnSolidWallTile> UN_SOLID_TILE = TILE_REGISTRY.register("unsolidwall", UnSolidWallTile::new);

    public static void init() {}
}
