package org.mangorage.gridgame.registry.core;

import org.mangorage.gridgame.api.grid.ITile;
import org.mangorage.gridgame.api.sound.ISound;

public class Registries {
    public static final Registry<ITile> TILE_REGISTRY = Registry.create(ITile.class);
    public static final Registry<ISound> SOUND_REGISTRY = Registry.create(ISound.class);

    public static void init() {
        TILE_REGISTRY.register();
        SOUND_REGISTRY.register();
    }
}
