package org.mangorage.gridgame.client.core;

import org.mangorage.gridgame.common.core.registry.Holder;
import org.mangorage.gridgame.common.world.Tile;
import org.mangorage.gridgame.common.world.TileEntity;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class TileRendererManager {
    private final Map<Tile, ITileRenderer<? extends Tile, ? extends TileEntity>> renderers = new HashMap<>();


    public <E extends TileEntity, T extends Tile> void register(Holder<T> tileHolder, Class<E> tileEntity, ITileRenderer<T, E> renderer) {
        var tile = tileHolder.get();
        if (renderers.containsKey(tile))
            throw new IllegalStateException("Cannot register Tile Renderer for %s, already has one registered".formatted(tileHolder.getID()));

        renderers.put(tile, renderer);
    }

    public <T extends Tile> void register(Holder<T> tileHolder, ITileRenderer<T, TileEntity> renderer) {
        register(tileHolder, null, renderer);
    }

    @SuppressWarnings("unchecked")
    public ITileRenderer<Tile, TileEntity> getRenderer(Tile tile) {
        return (ITileRenderer<Tile, TileEntity>) renderers.get(tile);
    }

    @FunctionalInterface
    public interface ITileRenderer<T extends Tile, TE extends TileEntity> {
        void render(Graphics graphics, T tile, TE tileEntity, int x, int y, int offsetX, int offsetY, int width, int height);
    }
}