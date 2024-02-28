package org.mangorage.gridgame.client;

import org.mangorage.gridgame.client.core.TileRendererManager;
import org.mangorage.gridgame.client.screen.RenderableScreen;
import org.mangorage.gridgame.common.BootStrap;
import org.mangorage.gridgame.common.Events;
import org.mangorage.gridgame.common.world.entities.Player;
import org.mangorage.gridgame.server.GridGameServer;
import org.mangorage.mangonetwork.core.Connection;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class GridGameClient implements KeyListener, MouseListener, MouseWheelListener {
    private static GridGameClient INSTANCE;

    public static void init(Connection connection) {
        if (GridGameServer.getInstance() != null)
            throw new IllegalStateException("Cant start Client... Already started server...");

        INSTANCE = new GridGameClient(connection);

        BootStrap.init(false);
    }

    public static GridGameClient getInstance() {
        return INSTANCE;
    }

    private final TileRendererManager renderManager = new TileRendererManager();
    private final Connection connection;
    private final ClientLevel clientLevel;
    private final Player player;

    private GridGameClient(Connection connection) {
        this.connection = connection;
        this.clientLevel = new ClientLevel();
        this.player = new Player(clientLevel);
        RenderableScreen.create();

        Events.RENDER_EVENT.addListener(e -> {
            clientLevel.render(e.graphics());
        });
    }

    public TileRendererManager getRenderManager() {
        return renderManager;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.moveRight();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

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

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    public ClientLevel getLevel() {
        return clientLevel;
    }

    public Connection getConnection() {
        return connection;
    }
}
