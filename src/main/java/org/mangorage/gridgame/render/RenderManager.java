package org.mangorage.gridgame.render;

import org.mangorage.gridgame.api.grid.ITile;
import org.mangorage.gridgame.api.grid.ITileEntity;
import org.mangorage.gridgame.api.render.IRenderer;
import org.mangorage.gridgame.game.tiles.entities.TileEntity;

import java.util.HashMap;
import java.util.Map;

public final class RenderManager {
    private static final RenderManager MANAGER = new RenderManager();

    public static RenderManager getInstance() {
        return MANAGER;
    }

    private RenderManager() {}

    private final Map<ITile, IRenderer<? extends ITile, ? extends ITileEntity>> rendererMap = new HashMap<>();


    public <E extends ITileEntity, T extends ITile> void register(T tile, Class<E> tileEntity, IRenderer<T, E> renderer) {
        rendererMap.put(tile, renderer);
    }

    public <T extends ITile> void register(T tile, IRenderer<T, TileEntity> renderer) {
        rendererMap.put(tile, renderer);
    }

    public IRenderer getRenderer(ITile tile) {
        return rendererMap.get(tile);
    }

}
