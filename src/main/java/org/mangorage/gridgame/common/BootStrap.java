package org.mangorage.gridgame.common;

import org.mangorage.gridgame.client.Renderers;
import org.mangorage.gridgame.common.events.LoadEvent;
import org.mangorage.gridgame.common.events.RegisterEvent;
import org.mangorage.gridgame.common.events.RegisterRenderersEvent;
import org.mangorage.gridgame.common.registry.TileRegistry;
import org.mangorage.mangonetwork.Packets;

public class BootStrap {
    private static boolean initiated = false;

    public static void init(boolean server) {
        if (initiated)
            throw new IllegalStateException("Already initialized bootstrap");

        initiated = true;

        // Handle mod loading.... then proceed to invoke events...

        Packets.init();
        TileRegistry.init();

        Events.LOAD_EVENT.trigger(new LoadEvent());
        Events.REGISTER_EVENT.trigger(new RegisterEvent());

        if (!server) {
            Events.REGISTER_RENDERERS.trigger(new RegisterRenderersEvent());
            Renderers.init();
        }
    }
}
