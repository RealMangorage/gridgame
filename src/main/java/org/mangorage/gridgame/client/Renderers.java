package org.mangorage.gridgame.client;

import org.mangorage.gridgame.client.core.CacheAPI;
import org.mangorage.gridgame.client.core.TileRendererManager;
import org.mangorage.gridgame.common.registry.TileRegistry;
import org.mangorage.gridgame.common.world.tileentity.PlayerTileEntity;
import org.mangorage.gridgame.common.world.tileentity.SolidWallTileEntity;

import java.awt.*;

public class Renderers {
    private static final TileRendererManager MANAGER = TileRendererManager.getInstance();

    static {
        MANAGER.register(
                TileRegistry.SOLD_TILE,
                SolidWallTileEntity.class,
                (graphics, tile, entity, x, y, oX, oY, w, h) -> {
                    graphics.setColor(Color.ORANGE);
                    if (entity.isUseCobble()) {
                        graphics.drawImage(CacheAPI.getInternalImage("/assets/cobblestone.png"), x * w, y * h, w, h, null);
                    } else {
                        graphics.drawImage(CacheAPI.getInternalImage("/assets/stone.png"), x * w, y * h, w, h, null);
                    }
                }
        );

        MANAGER.register(
                TileRegistry.PLAYER_TILE,
                PlayerTileEntity.class,
                (graphics, tile, entity, x, y, oX, oY, w, h) -> {
                    graphics.setColor(Color.RED);
                    graphics.fillRect(x * w, y * h, w, h);
                    if (entity.getUsername() != null)
                        graphics.drawString(entity.getUsername(), x * w, y * h);
                }
        );
    }

    public static void init() {
    }
}
