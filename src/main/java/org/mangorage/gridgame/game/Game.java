package org.mangorage.gridgame.game;

import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.mangorage.gridgame.api.SoundAPI;
import org.mangorage.gridgame.api.Util;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game extends Thread implements KeyListener, MouseWheelListener {
    private static final ExecutorService SAVE_EXECUTOR = Executors.newSingleThreadExecutor();

    public static void init() {
        Tiles.init();
        TileRenderers.init();
        GAME.load();
        GAME.start();
    }


    private static final Game GAME = new Game();

    public static Game getInstance() {
        return GAME;
    }

    private final Grid grid = new Grid(10_000, 10_000, 160, 85);
    private final Grid entityGrid = grid.createNewLayer();
    private final Player player = new Player();

    private int scale = 40;
    private boolean running = true;
    private int tickRate = 20;
    private int ticks = 0;

    private GameState state = GameState.LOADING;

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
        if (state == GameState.READY) {
            ticks++;
            grid.tick();
        }
    }

    public int getTicks() {
        return ticks;
    }

    public int getScale() {
        return scale;
    }

    public GameState getGameState() {
        return state;
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
        if (state == GameState.READY) {
            grid.render(graphics);
        } else if (state == GameState.LOADING) {
            graphics.setColor(Color.WHITE);
            graphics.drawString("GAME LOADING... PLEASE WAIT", 20, 20);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (state == GameState.READY) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_S -> player.moveDown();
                case KeyEvent.VK_W -> player.moveUp();
                case KeyEvent.VK_A -> player.moveLeft();
                case KeyEvent.VK_D -> player.moveRight();
                case KeyEvent.VK_SPACE -> {
                    SoundAPI.playSound("/assets/quick.wav");
                    grid.setTile(player.getX(), player.getY(), Tiles.UN_SOLID_TILE);
                }
                case KeyEvent.VK_F1 -> SAVE_EXECUTOR.execute(this::save);
                case KeyEvent.VK_F4 -> SoundAPI.playSound("/assets/toilet_flush.wav");
                case KeyEvent.VK_UP -> grid.updateBounds(grid.getBoundsX(), grid.getBoundsY() - 1);
                case KeyEvent.VK_DOWN -> grid.updateBounds(grid.getBoundsX(), grid.getBoundsY() + 1);
                case KeyEvent.VK_G -> grid.updateBounds(grid.getBoundsX() - 1, grid.getBoundsY());
                case KeyEvent.VK_H -> grid.updateBounds(grid.getBoundsX() + 1, grid.getBoundsY());
            }
        }
    }

    private void save() {
        File gameDataCompressed = new File("gamedata.dat");
        File gameDataTilesCompressed = new File("gamedata_tiles.dat");
        if (Files.exists(gameDataTilesCompressed.toPath()))
            if (!gameDataTilesCompressed.delete())
                System.out.println("Error while attempting to delete previous save data...");

        if (Files.exists(gameDataTilesCompressed.toPath()))
            if (!gameDataCompressed.delete())
                System.out.println("Error while attempting to delete previous save data...");

        File gameData = new File("gamedata.nbt");
        File gameDataTiles = new File("gamedata_tiles.raw");

        CompoundTag gameDataTag = new CompoundTag();
        ListTag<CompoundTag> layerTags = new ListTag<>(CompoundTag.class);

        Grid grid = getGrid();
        List<byte[][]> tilesData = new ArrayList<>();
        while (grid != null) {
            CompoundTag layer = new CompoundTag();
            grid.save(layer);
            layerTags.add(layer);
            tilesData.add(grid.getGridData());
            grid = grid.getNextLayer();
        }

        CompoundTag playerDataTag = new CompoundTag();
        playerDataTag.putInt("x", player.getX());
        playerDataTag.putInt("y", player.getY());

        gameDataTag.put("layers", layerTags);
        gameDataTag.put("playerData", playerDataTag);

        try {
            NBTUtil.write(gameDataTag, gameData, true);

            Util.serializeIntArray2D(tilesData, gameDataTiles);
            Util.compressFile(gameDataTiles, "gamedata_tiles.dat");
            Util.compressFile(gameData, "gamedata.dat");
            gameDataTiles.delete();
            gameData.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void load() {
        File gameDataTiles = new File("gamedata_tiles.dat");
        File gameData = new File("gamedata.dat");


        if (Files.exists(gameDataTiles.toPath()) && Files.exists(gameData.toPath())) {
            try {
                var gameDataTilesDecompressed = Util.decompressFile(gameDataTiles, "gamedata_tiles.raw");
                var gameDataDecompressed = Util.decompressFile(gameData, "gamedata.nbt");


                var gridData = Util.deserializeIntArray2D(gameDataTilesDecompressed);
                gameDataTilesDecompressed.delete();
                Grid gridLayer = getGrid();
                for (byte[][] grid : gridData) {
                    gridLayer.load(grid);
                    gridLayer = gridLayer.getNextLayer();
                }

                CompoundTag gameDataTag = (CompoundTag) NBTUtil.read(gameDataDecompressed, true).getTag();
                gameDataDecompressed.delete();
                ListTag<CompoundTag> layers = gameDataTag.getListTag("layers").asCompoundTagList();

                Grid grid = getGrid();
                for (CompoundTag layer : layers) {
                    grid.load(layer);
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
            } finally {
                state = GameState.READY;
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
