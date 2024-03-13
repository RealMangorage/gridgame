package org.mangorage.gridgame.client.screen;

import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.gridgame.common.Events;
import org.mangorage.gridgame.common.events.RenderEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameScreen extends JPanel {
    public static long lastUpdateMSLength = 0;
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private static final DecimalFormat formatter = new DecimalFormat("#,###");

    private static final int WINDOW_WIDTH = 1080;
    private static final int WINDOW_HEIGHT = 720;

    private final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor((r) -> new Thread(r, "Render-Thread"));
    private BufferedImage IMAGE;
    private Graphics2D graphics2D;

    private GameScreen() {
        SERVICE.scheduleAtFixedRate(this::repaint, 0,  (long) (((double) 1/60) * 1000), TimeUnit.MILLISECONDS);
    }

    public void paintExtra(Graphics graphics) {
        var cl = GridGameClient.getInstance();
        if (cl != null && cl.getPlayer() != null) {
            var plr = cl.getPlayer().getPos();
            graphics.drawString("X: %s Y: %s".formatted(plr.x(), plr.y()), 10, 10);
            graphics.drawString("Render Time per frame: %s".formatted(lastUpdateMSLength), 10, 20);
        }
    }



    @Override
    protected void paintComponent(Graphics graphics) {
        if (IMAGE == null) {
            this.IMAGE = getGraphicsConfiguration().createCompatibleImage(WINDOW_WIDTH, WINDOW_HEIGHT);
            this.graphics2D = IMAGE.createGraphics();
        }

        long start = System.currentTimeMillis();

        graphics2D.clearRect(0, 0, getWidth(), getHeight());
        Events.RENDER_EVENT.trigger(new RenderEvent(graphics2D));
        paintExtra(graphics2D);
        graphics.drawImage(IMAGE, 0, 0, null);

        lastUpdateMSLength = (System.currentTimeMillis() - start);
    }

    public static void create() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Grid Game! By MangoRage");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GameScreen gameScreen = new GameScreen();
            gameScreen.setBackground(Color.BLACK);
            frame.add(gameScreen);
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setVisible(true);
            frame.addKeyListener(GridGameClient.getInstance());
            frame.addMouseWheelListener(GridGameClient.getInstance());
            frame.addMouseListener(GridGameClient.getInstance());
        });
    }
}
