package org.mangorage.gridgame.core.registry;

import org.mangorage.gridgame.core.grid.entities.Entity;
import org.mangorage.gridgame.core.grid.entities.EntityType;
import org.mangorage.gridgame.core.grid.tiles.Tile;
import org.mangorage.gridgame.core.sound.ISound;

public class Registries {
    public static final Registry<Tile> TILE_REGISTRY = Registry.create("tile");
    public static final Registry<EntityType<? extends Entity>> ENTITY_REGISTRY = Registry.create("entity_type");
    public static final Registry<ISound> SOUND_REGISTRY = Registry.create("sound");

    public static void init() {
        TILE_REGISTRY.register();
        ENTITY_REGISTRY.register();
        SOUND_REGISTRY.register();
    }
}
