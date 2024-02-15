package org.mangorage.gridgame.api;

import java.awt.*;

public interface IRender {

    void render(Graphics graphics, int x, int y);

    default void render(Graphics graphics) {
        render(graphics, 0, 0);
    }
}
