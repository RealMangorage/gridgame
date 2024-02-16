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
        grid.setTile(x, y, Tiles.EMPTY_TILE);
        grid.setTile(newX, newY, Tiles.PLAYER_TILE);

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
        grid.setTile(x, y, Tiles.PLAYER_TILE);
    }

    public void removeTile(Grid grid) {
        grid.setTile(x, y, Tiles.EMPTY_TILE);
    }

    public void moveDown() {
        int newY = y + 1;
        Grid grid = Game.getInstance().getGrid();
        if (newY < grid.getSizeY()) {
            ITile walkTo = grid.getGridTile(x, newY).getTile();
            if (!walkTo.isSolid(x, newY)) {
                updatePosition(Game.getInstance().getGrid(1), x, newY);
            }
        }
    }

    public void moveUp() {
        int newY = y - 1;
        Grid grid = Game.getInstance().getGrid();
        if (newY != -1) {
            ITile walkTo = grid.getGridTile(x, newY).getTile();
            if (!walkTo.isSolid(x, newY)) {
                updatePosition(Game.getInstance().getGrid(1), x, newY);
            }
        }
    }

    public void moveLeft() {
        int newX = x - 1;
        Grid grid = Game.getInstance().getGrid();
        if (newX != -1) {
            ITile walkTo = grid.getGridTile(newX, y).getTile();
            if (!walkTo.isSolid(newX, y)) {
                updatePosition(Game.getInstance().getGrid(1), newX, y);
            }
        }
    }

    public void moveRight() {
        int newX = x + 1;
        Grid grid = Game.getInstance().getGrid();
        if (newX < grid.getSizeX()) {
            ITile walkTo = grid.getGridTile(newX, y).getTile();
            if (!walkTo.isSolid(newX, y)) {
                updatePosition(Game.getInstance().getGrid(1), newX, y);
            }
        }
    }
}
