package org.mangorage.gridgame.registry;

import org.mangorage.gridgame.api.CacheAPI;
import org.mangorage.gridgame.render.RenderManager;
import org.mangorage.gridgame.game.tiles.entities.UnSolidWallTileEntity;

import java.awt.*;

public class TileRenderers {
    static {
        var manager = RenderManager.getInstance();
        manager.register(
                Tiles.WALL_TILE.get(),
                (graphics, tile, tileEntity, x, y, offsetX, offsetY, width, height) -> {
                    graphics.setColor(Color.ORANGE);
                    graphics.fillRect((x - offsetX) * width, (y - offsetY) * height, width, height);
                }
        );
        manager.register(
                Tiles.PLAYER_TILE.get(),
                (graphics, tile, tileEntity, x, y, offsetX, offsetY, width, height) -> {
                    graphics.setColor(Color.BLUE);
                    graphics.fillRect((x - offsetX) * width, (y - offsetY) * height, width, height);
                }
        );
        manager.register(
                Tiles.UN_SOLID_TILE.get(),
                UnSolidWallTileEntity.class,
                (graphics, tile, tileEntity, x, y, offsetX, offsetY, width, height) -> {
                    var texture = tileEntity.isSolid() ? CacheAPI.getInternalImage("/assets/stone.png") : CacheAPI.getInternalImage("/assets/cobblestone.png");
                    //var texture = CacheAPI.getInternalImage("/assets/cobblestone.png");
                    graphics.drawImage(texture, (x - offsetX) * width, (y - offsetY) * height, width, height, null);
                }
        );
    }

    public static void init() {}
}
