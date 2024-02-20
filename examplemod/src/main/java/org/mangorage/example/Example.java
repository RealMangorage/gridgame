package org.mangorage.example;

import org.mangorage.example.tiles.TileRegistry;
import org.mangorage.gridgame.core.classloading.EntryPoint;
import org.mangorage.gridgame.core.events.Events;
import org.mangorage.gridgame.core.render.TileRendererManager;
import org.mangorage.gridgame.registry.Tiles;

import java.awt.*;
import java.util.Random;

@EntryPoint
public class Example {
    public Example() {
        Random random = new Random();
        Events.REGISTRY_EVENT.addListener(registryEvent -> {
            TileRegistry.init();
        });

        Events.REGISTER_TILE_RENDERER_EVENT.addListener(tileRenderRegisterEvent -> {
            var manager = TileRendererManager.getInstance();
            manager.register(
                    TileRegistry.EXAMPLE_TILE,
                    (graphics, tile, tileEntity, x, y, offsetX, offsetY, width, height) -> {
                        graphics.setColor(Color.GREEN);
                        graphics.fillRect((x - offsetX) * width, (y - offsetY) * height, width, height);
                    }
            );
        });

        Events.TILE_CHANGE_EVENT.addListener(tileChangeEvent -> {
            if (random.nextInt(100) == 10 && tileChangeEvent.getTile() == Tiles.EMPTY_TILE.get() && tileChangeEvent.getTilePos().z() == 0) {
                tileChangeEvent.setTile(TileRegistry.EXAMPLE_TILE.get());
            }
        });
    }
}
