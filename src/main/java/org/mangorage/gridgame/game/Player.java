package org.mangorage.gridgame.game;

import org.mangorage.gridgame.api.grid.Grid;
import org.mangorage.gridgame.api.grid.ITile;
import org.mangorage.gridgame.registry.Tiles;

public class Player {
    private int x, y = 0;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void updatePosition(Grid grid, int newX, int newY) {
        grid.setTile(x, y, 1, Tiles.EMPTY_TILE.get());
        grid.setTile(newX, newY, 1, Tiles.PLAYER_TILE.get());

        this.x = newX;
        this.y = newY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setTile(Grid grid) {
        grid.setTile(x, y, 1, Tiles.PLAYER_TILE.get());
    }

    public void removeTile(Grid grid) {
        grid.setTile(x, y, 1, Tiles.EMPTY_TILE.get());
    }

    public void moveDown() {
        int newY = y + 1;
        Grid grid = Game.getInstance().getGrid();
        if (newY < grid.getSizeY()) {
            ITile walkTo = grid.getGridTile(x, newY, 0).getTile();
            if (!walkTo.isSolid(grid, x, newY)) {
                updatePosition(grid, x, newY);
            }
        }
    }

    public void moveUp() {
        int newY = y - 1;
        Grid grid = Game.getInstance().getGrid();
        if (newY != -1) {
            ITile walkTo = grid.getGridTile(x, newY, 0).getTile();
            if (!walkTo.isSolid(grid, x, newY)) {
                updatePosition(grid, x, newY);
            }
        }
    }

    public void moveLeft() {
        int newX = x - 1;
        Grid grid = Game.getInstance().getGrid();
        if (newX != -1) {
            ITile walkTo = grid.getGridTile(newX, y, 0).getTile();
            if (!walkTo.isSolid(grid, newX, y)) {
                updatePosition(grid, newX, y);
            }
        }
    }

    public void moveRight() {
        int newX = x + 1;
        Grid grid = Game.getInstance().getGrid();
        if (newX < grid.getSizeX()) {
            ITile walkTo = grid.getGridTile(newX, y, 0).getTile();
            if (!walkTo.isSolid(grid, newX, y)) {
                updatePosition(grid, newX, y);
            }
        }
    }
}
