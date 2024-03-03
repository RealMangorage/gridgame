package org.mangorage.gridgame.client;

import org.mangorage.gridgame.client.core.CacheAPI;
import org.mangorage.gridgame.client.core.TileRendererManager;
import org.mangorage.gridgame.common.registry.TileRegistry;
import org.mangorage.gridgame.common.world.tileentity.SolidWallTileEntity;

import java.awt.*;

public class Renderers {
    private static final TileRendererManager MANAGER = GridGameClient.getInstance().getRenderManager();

    static {
        MANAGER.register(
                TileRegistry.SOLD_TILE,
                SolidWallTileEntity.class,
                (graphics, tile, entity, x, y, oX, oY, w, h) -> {
                    graphics.setColor(Color.ORANGE);
                    if (entity.isUseCobble()) {
                        graphics.drawImage(CacheAPI.getInternalImage("/assets/cobblestone.png"), x * 16, y * 16, 16, 16, null);
                    } else {
                        graphics.drawImage(CacheAPI.getInternalImage("/assets/stone.png"), x * 16, y * 16, 16, 16, null);
                    }
                }
        );

        MANAGER.register(
                TileRegistry.PLAYER_TILE,
                (graphics, tile, entity, x, y, oX, oY, w, h) -> {
                    graphics.setColor(Color.RED);
                    graphics.fillRect(x * 16, y * 16, 16, 16);
                }
        );
    }

    public static void init() {
    }
}
