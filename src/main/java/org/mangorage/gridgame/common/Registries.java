package org.mangorage.gridgame.common;

import org.mangorage.gridgame.common.core.registry.Registry;
import org.mangorage.gridgame.common.world.Tile;

public class Registries {
    public static final Registry<Tile> TILE_REGISTRY = Registry.create("tiles");
}
