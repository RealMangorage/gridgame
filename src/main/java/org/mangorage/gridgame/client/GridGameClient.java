package org.mangorage.gridgame.client;

import org.mangorage.gridgame.client.screen.GameScreen;
import org.mangorage.gridgame.client.world.ClientLevel;
import org.mangorage.gridgame.client.world.entities.LocalPlayer;
import org.mangorage.gridgame.common.Events;
import org.mangorage.gridgame.common.core.Direction;
import org.mangorage.gridgame.common.world.entities.Player;
import org.mangorage.mangonetwork.core.connection.Connection;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class GridGameClient implements KeyListener, MouseListener, MouseWheelListener {
    private static GridGameClient INSTANCE;

    public static void init(Connection connection, String username) {
        if (INSTANCE != null) return;

        INSTANCE = new GridGameClient(connection, username);
    }

    public static GridGameClient getInstance() {
        return INSTANCE;
    }

    private final Connection connection;
    private final ClientLevel clientLevel;
    private final Player player;

    private GridGameClient(Connection connection, String username) {
        this.connection = connection;
        this.clientLevel = new ClientLevel();
        this.player = new LocalPlayer(clientLevel, username);
        GameScreen.create();

        Events.RENDER_EVENT.addListener(e -> {
            clientLevel.render(e.graphics());
        });
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.move(Direction.UP);
            case KeyEvent.VK_S -> player.move(Direction.DOWN);
            case KeyEvent.VK_A -> player.move(Direction.LEFT);
            case KeyEvent.VK_D -> player.move(Direction.RIGHT);
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
