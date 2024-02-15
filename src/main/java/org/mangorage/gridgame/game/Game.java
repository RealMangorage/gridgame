package org.mangorage.gridgame.game;

import org.mangorage.gridgame.api.grid.Grid;
import org.mangorage.gridgame.api.IRender;
import org.mangorage.gridgame.registry.TileRenderers;
import org.mangorage.gridgame.registry.Tiles;
import org.mangorage.gridgame.render.RenderableScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Game extends Thread implements IRender, KeyListener, MouseWheelListener {
    public static void init() {
        Tiles.init();
        TileRenderers.init();
        GAME.start();
    }

    private static final Game GAME = new Game();

    public static Game getInstance() {
        return GAME;
    }

    private final Grid grid = new Grid(160, 85, 160, 85);
    private final Grid entityGrid = grid.createNewLayer();

    private final Player player = new Player();
    private int scale = 64;
    private boolean running = true;
    private int tickRate = 20;
    private int ticks = 0;

    public Game() {
        player.setX(1);
        player.setY(1);
        player.setTile(entityGrid);
        grid.setTile(5, 5, Tiles.UN_SOLID_TILE);
        RenderableScreen.create();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep((int) (((double) 1 / tickRate) * 1000));
                tick();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void tick() {
        ticks++;
        grid.tick();
    }

    public int getTicks() {
        return ticks;
    }

    public int getScale() {
        return scale;
    }

    public Grid getGrid() {
        return getGrid(0);
    }

    public Player getPlayer() {
        return player;
    }

    public Grid getGrid(int layer) {
        if (layer == 0) return grid;
        if (layer == 1) return entityGrid;
        return null;
    }


    @Override
    public void render(Graphics graphics, int x, int y) {
        grid.render(graphics);
        Grid layer = grid.getNextLayer();
        while (layer != null) {
            layer.render(graphics);
            layer = layer.getNextLayer();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_S -> player.moveDown();
            case KeyEvent.VK_W -> player.moveUp();
            case KeyEvent.VK_A -> player.moveLeft();
            case KeyEvent.VK_D -> player.moveRight();
            case KeyEvent.VK_SPACE -> grid.setTile(player.getX(), player.getY(), Tiles.UN_SOLID_TILE);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        var val = e.getWheelRotation();
        if (val > 0 && scale > 16) {
            scale = scale - 2;
            grid.updateBounds(scale);
        } else if (val < 0 && scale < 40) {
            scale = scale + 2;
            grid.updateBounds(scale);
        }
    }
}
