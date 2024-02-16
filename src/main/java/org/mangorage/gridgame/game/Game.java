package org.mangorage.gridgame.game;

import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.mangorage.gridgame.api.SoundAPI;
import org.mangorage.gridgame.api.grid.Grid;
import org.mangorage.gridgame.registry.TileRegistry;
import org.mangorage.gridgame.registry.TileRenderers;
import org.mangorage.gridgame.registry.Tiles;
import org.mangorage.gridgame.render.RenderableScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Game extends Thread implements KeyListener, MouseWheelListener {
    public static void init() {
        TileRegistry.getInstance().createTileLookup();
        Tiles.init();
        TileRenderers.init();
        GAME.load();
        GAME.start();
    }


    private static final Game GAME = new Game();

    public static Game getInstance() {
        return GAME;
    }

    private final Grid grid = new Grid(160, 85, 160, 85);
    private final Grid entityGrid = grid.createNewLayer();

    private final Player player = new Player();
    private int scale = 40;
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


    public void render(Graphics graphics) {
        grid.render(graphics);
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
            case KeyEvent.VK_SPACE -> {
                SoundAPI.playSound("/assets/quick.wav");
                grid.setTile(player.getX(), player.getY(), Tiles.UN_SOLID_TILE);
            }
            case KeyEvent.VK_F1 -> save();
            case KeyEvent.VK_F4 -> SoundAPI.playSound("/assets/toilet_flush.wav");
            case KeyEvent.VK_UP -> grid.updateBounds(grid.getBoundsX(), grid.getBoundsY() - 1);
            case KeyEvent.VK_DOWN -> grid.updateBounds(grid.getBoundsX(), grid.getBoundsY() + 1);
            case KeyEvent.VK_G -> grid.updateBounds(grid.getBoundsX() - 1, grid.getBoundsY());
            case KeyEvent.VK_H -> grid.updateBounds(grid.getBoundsX() + 1, grid.getBoundsY());
        }
    }

    private void save() {
        File gameData = new File("gamedata.nbt");
        if (Files.exists(gameData.toPath()))
            if (!gameData.delete())
                System.out.println("Error while attempting to delete previous save data...");


        CompoundTag gameDataTag = new CompoundTag();
        ListTag<CompoundTag> layerTags = new ListTag<>(CompoundTag.class);
        CompoundTag tag = new CompoundTag();
        Grid grid = getGrid();

        while (grid != null) {
            grid.save(tag);

            layerTags.add(tag);
            grid = grid.getNextLayer();
            tag = new CompoundTag();
        }

        ListTag<CompoundTag> tags = new ListTag<>(CompoundTag.class);
        TileRegistry.getInstance().getTileLookup().forEach((k, v) -> {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("id", k);
            compoundTag.putShort("idShort", v);
            tags.add(compoundTag);
        });

        CompoundTag playerDataTag = new CompoundTag();
        playerDataTag.putInt("x", player.getX());
        playerDataTag.putInt("y", player.getY());


        gameDataTag.put("layers", layerTags);
        gameDataTag.put("lookup", tags);
        gameDataTag.put("playerData", playerDataTag);

        try {
            NBTUtil.write(gameDataTag, gameData, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void load() {
        File gameData = new File("gamedata.nbt");
        if (Files.exists(gameData.toPath())) {
            try {
                CompoundTag gameDataTag = (CompoundTag) NBTUtil.read(gameData, true).getTag();
                var lookup = TileRegistry.getTileLookupFromTag(gameDataTag.getListTag("lookup").asCompoundTagList());
                ListTag<CompoundTag> layers = gameDataTag.getListTag("layers").asCompoundTagList();
                Grid grid = getGrid();
                for (CompoundTag layer : layers) {
                    grid.load(layer, lookup);
                    if (grid.getNextLayer() != null) {
                        grid = grid.getNextLayer();
                    }
                }

                CompoundTag playerData = gameDataTag.getCompoundTag("playerData");
                int x = playerData.getInt("x");
                int y = playerData.getInt("y");
                player.updatePosition(entityGrid, x, y);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
        } else if (val < 0 && scale < 40) {
            scale = scale + 2;
        }
    }
}
