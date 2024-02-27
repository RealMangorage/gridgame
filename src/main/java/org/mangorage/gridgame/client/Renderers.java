package org.mangorage.gridgame.client;

import org.mangorage.gridgame.client.core.TileRendererManager;
import org.mangorage.gridgame.common.registry.TileRegistry;

import java.awt.*;

public class Renderers {
    private static final TileRendererManager MANAGER = GridGameClient.getInstance().getRenderManager();

    static {
        MANAGER.register(
                TileRegistry.SOLD_TILE,
                (graphics, tile, entity, x, y, oX, oY, w, h) -> {
                    graphics.setColor(Color.ORANGE);
                    graphics.fillRect(x * 16, y * 16, 16, 16);
                }
        );
    }

    public static void init() {
    }
}
