package org.mangorage.gridgame.registry;

import org.mangorage.gridgame.game.tiles.EmptyTile;
import org.mangorage.gridgame.game.tiles.PlayerTile;
import org.mangorage.gridgame.game.tiles.UnSolidWallTile;
import org.mangorage.gridgame.game.tiles.WallTile;

public class Tiles {
    private static final TileRegistry REGISTRY = TileRegistry.getInstance();


    public static final EmptyTile EMPTY_TILE = REGISTRY.register(0, new EmptyTile());
    public static final PlayerTile PLAYER_TILE = REGISTRY.register(1, new PlayerTile());
    public static final WallTile WALL_TILE = REGISTRY.register(2, new WallTile());
    public static final UnSolidWallTile UN_SOLID_TILE = REGISTRY.register(3, new UnSolidWallTile());

    public static void init() {}
}
