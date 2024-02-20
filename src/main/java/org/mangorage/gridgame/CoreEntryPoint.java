package org.mangorage.gridgame;

import org.mangorage.gridgame.core.classloading.EntryPoint;
import org.mangorage.gridgame.core.events.Events;
import org.mangorage.gridgame.registry.Sounds;
import org.mangorage.gridgame.registry.TileRenderers;
import org.mangorage.gridgame.registry.Tiles;

@EntryPoint
public class CoreEntryPoint {
    public CoreEntryPoint() {
        Events.REGISTRY_EVENT.addListener(registryEvent -> {
            Tiles.init();
            Sounds.init();
        });

        Events.REGISTER_TILE_RENDERER_EVENT.addListener(tileRenderRegisterEvent -> {
            TileRenderers.init();
        });

        Events.TILE_CHANGE_EVENT.addListener(tileChangeEvent -> {
            //if (!tileChangeEvent.isCancelled() && tileChangeEvent.getTile() != Tiles.PLAYER_TILE.get()) {
                //tileChangeEvent.setTile(Tiles.EMPTY_TILE.get());
            //}
        });
    }
}
