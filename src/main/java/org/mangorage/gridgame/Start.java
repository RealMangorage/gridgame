package org.mangorage.gridgame;

import org.mangorage.gridgame.game.Game;
import org.mangorage.gridgame.registry.Sounds;
import org.mangorage.gridgame.registry.TileRenderers;
import org.mangorage.gridgame.registry.Tiles;
import org.mangorage.gridgame.core.registry.Registries;

public class Start {
    public static void main(String[] args) {
        Tiles.init();
        Sounds.init();

        Registries.init();

        TileRenderers.init();
        Game.init();
    }
}
