package org.mangorage.gridgame.api.render;

import java.awt.*;

@FunctionalInterface
public interface IRenderer<T, TE> {
    void render(Graphics graphics, T tile, TE tileEntity, int x, int y, int offsetX, int offsetY, int width, int height);
}
