package org.mangorage.gridgame.registry;

import org.mangorage.gridgame.game.tiles.EmptyTile;
import org.mangorage.gridgame.game.tiles.PlayerTile;
import org.mangorage.gridgame.game.tiles.UnSolidTile;
import org.mangorage.gridgame.game.tiles.WallTile;

public class Tiles {
    public static final PlayerTile PLAYER_TILE = new PlayerTile();
    public static final EmptyTile EMPTY_TILE = new EmptyTile();
    public static final WallTile WALL_TILE = new WallTile();
    public static final UnSolidTile UN_SOLID_TILE = new UnSolidTile();

    public static void init() {
    }
}
