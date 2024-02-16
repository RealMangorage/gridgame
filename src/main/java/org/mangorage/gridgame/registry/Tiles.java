package org.mangorage.gridgame.registry;

import org.mangorage.gridgame.game.tiles.EmptyTile;
import org.mangorage.gridgame.game.tiles.PlayerTile;
import org.mangorage.gridgame.game.tiles.UnSolidWallTile;
import org.mangorage.gridgame.game.tiles.WallTile;

public class Tiles {
    private static final TileRegistry REGISTRY = TileRegistry.getInstance();

    public static final PlayerTile PLAYER_TILE = REGISTRY.register("player", new PlayerTile());
    public static final EmptyTile EMPTY_TILE = REGISTRY.register("air", new EmptyTile());
    public static final WallTile WALL_TILE = REGISTRY.register("wall", new WallTile());
    public static final UnSolidWallTile UN_SOLID_TILE = REGISTRY.register("unsolidwall", new UnSolidWallTile());

    public static void init() {}
}
