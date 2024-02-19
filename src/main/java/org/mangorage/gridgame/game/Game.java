package org.mangorage.gridgame.game;

import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.mangorage.gridgame.core.Util;
import org.mangorage.gridgame.core.grid.Grid;
import org.mangorage.gridgame.registry.Sounds;
import org.mangorage.gridgame.registry.Tiles;
import org.mangorage.gridgame.render.RenderableScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game extends Thread implements KeyListener, MouseWheelListener, MouseListener {
    private static final ExecutorService SAVE_EXECUTOR = Executors.newSingleThreadExecutor();

    public static void init() {
        GAME.load();
        GAME.start();
    }


    private static final Game GAME = new Game();

    public static Game getInstance() {
        return GAME;
    }

    private final Grid grid = new Grid(10_000, 10_000, 2, 20, 20);
    private final Player player = new Player();

    private int scale = 40;
    private boolean running = true;
    private int tickRate = 20;
    private int ticks = 0;
    private long msPerTick = 0;
    private boolean debug = false;
    private boolean saving = false;
    private long saveStart = 0;
    private long saveLenghth = 0;
    private GameState state = GameState.LOADING;

    public Game() {
        player.setX(1);
        player.setY(1);
        player.setTile(grid);
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
        long start = System.currentTimeMillis();
        if (state == GameState.READY) {
            ticks++;
            grid.tick();
        }
        msPerTick = System.currentTimeMillis() - start;
    }

    public int getTicks() {
        return ticks;
    }

    public int getTickRate() {
        return tickRate;
    }

    public boolean isSaving() {
        return saving;
    }

    public long getSaveStartTime() {
        return saveStart;
    }

    public long getSaveLenghth() {
        return saveLenghth;
    }

    public long getLastMSPerTick() {
        return msPerTick;
    }

    public boolean showDebug() {
        return debug;
    }

    public int getScale() {
        return scale;
    }

    public GameState getGameState() {
        return state;
    }

    public Grid getGrid() {
        return grid;
    }

    public Player getPlayer() {
        return player;
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
                    Sounds.LASER.get().play();
                    grid.setTile(player.getX(), player.getY(), 0, Tiles.UN_SOLID_TILE.get());
                }
                case KeyEvent.VK_F1 -> SAVE_EXECUTOR.execute(this::save);
                case KeyEvent.VK_F4 -> Sounds.TOILET_FLUSH.get().play();
                case KeyEvent.VK_UP -> grid.updateBounds(grid.getBoundsX(), grid.getBoundsY() - 1);
                case KeyEvent.VK_DOWN -> grid.updateBounds(grid.getBoundsX(), grid.getBoundsY() + 1);
                case KeyEvent.VK_G -> grid.updateBounds(grid.getBoundsX() - 1, grid.getBoundsY());
                case KeyEvent.VK_H -> grid.updateBounds(grid.getBoundsX() + 1, grid.getBoundsY());
                case KeyEvent.VK_F3 -> debug = !debug;
            }
        }
    }

    private void save() {
        if (saving) return;
        this.saving = true;
        this.saveStart = System.currentTimeMillis();

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

        Grid grid = getGrid();
        byte[][][] tilesData = grid.getGridData();
        CompoundTag gridData = new CompoundTag();
        grid.save(gridData);

        CompoundTag playerDataTag = new CompoundTag();
        playerDataTag.putInt("x", player.getX());
        playerDataTag.putInt("y", player.getY());

        gameDataTag.put("gridData", gridData);
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
        } finally {
            this.saveLenghth = System.currentTimeMillis() - saveStart;
            this.saving = false;
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

                getGrid().load(gridData);

                CompoundTag gameDataTag = (CompoundTag) NBTUtil.read(gameDataDecompressed, true).getTag();
                gameDataDecompressed.delete();

                CompoundTag gridDataTag = gameDataTag.getCompoundTag("gridData");

                Grid grid = getGrid();
                grid.load(gridDataTag);

                CompoundTag playerData = gameDataTag.getCompoundTag("playerData");
                int x = playerData.getInt("x");
                int y = playerData.getInt("y");
                player.updatePosition(grid, x, y);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                state = GameState.READY;
            }
        } else {
            state = GameState.READY;
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

    @Override
    public void mouseClicked(MouseEvent e) {
        var pos = Util.getTilePosFromMouse(scale, e.getX(), e.getY(), grid.getOffsetX(player), grid.getOffsetY(player));
        grid.setTile(pos.x(), pos.y(), 0, Tiles.UN_SOLID_TILE.get());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
