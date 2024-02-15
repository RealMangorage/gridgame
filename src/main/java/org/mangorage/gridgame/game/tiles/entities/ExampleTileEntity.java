package org.mangorage.gridgame.game.tiles.entities;

public class ExampleTileEntity extends TileEntity {
    private int ticks = 0;
    private boolean solid = false;

    public ExampleTileEntity(int x, int y) {
        super(x, y);
    }

    @Override
    public void tick() {
        ticks++;
        if (ticks % 120 == 0) {
            solid = !solid;
        }
    }

    public boolean isSolid() {
        return solid;
    }
}
