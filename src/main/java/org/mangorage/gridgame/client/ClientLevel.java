package org.mangorage.gridgame.client;

import org.mangorage.gridgame.client.core.TileRendererManager;
import org.mangorage.gridgame.common.Registries;
import org.mangorage.gridgame.common.world.Level;
import org.mangorage.gridgame.common.world.Tile;
import org.mangorage.gridgame.common.world.TilePos;
import org.mangorage.mangonetwork.core.Connection;

import java.awt.*;

public class ClientLevel extends Level {
    private final Connection connection;
    private byte[][][] tiles;


    public ClientLevel(Connection connection) {
        this.connection = connection;
    }

    public void setTiles(int x, int y, int z) {
        this.tiles = new byte[z][x][y];
    }

    @Override
    public void setTile(TilePos pos, Tile tile, int flag) {
        if (flag == 2) {
            tiles[pos.z()][pos.x()][pos.y()] = Registries.TILE_REGISTRY.getID(tile);
        }
    }

    public void render(Graphics graphics) {
        if (tiles != null) {
            for (int z = 0; z < tiles.length; z++) {
                for (int x = 0; x < tiles[z].length ; x++) {
                    for (int y = 0; y < tiles[z][x].length; y++) {
                        var id = tiles[z][x][y];
                        var tile = Registries.TILE_REGISTRY.getObject(id);

                        TileRendererManager.ITileRenderer<Tile, ?> renderer = GridGameClient.getInstance().getRenderManager().getRenderer(tile);

                        if (renderer != null)
                            renderer.render(graphics, tile, null, x, y, 0, 0, 16, 16);
                    }
                }
            }
        }
    }
}
