package org.mangorage.gridgame.registry;

import org.mangorage.gridgame.core.sound.ISound;
import org.mangorage.gridgame.core.sound.Sound;
import org.mangorage.gridgame.core.registry.Holder;
import org.mangorage.gridgame.core.registry.Registries;
import org.mangorage.gridgame.core.registry.Registry;

public class Sounds {
    private static final Registry<ISound> SOUND_REGISTRY = Registries.SOUND_REGISTRY;

    public static final Holder<Sound> TOILET_FLUSH = SOUND_REGISTRY.register("toilet_flush", () -> new Sound("/assets/toilet_flush.wav"));
    public static final Holder<Sound> LASER = SOUND_REGISTRY.register("laser", () -> new Sound("/assets/quick.wav"));

    public static void init() {}
}
