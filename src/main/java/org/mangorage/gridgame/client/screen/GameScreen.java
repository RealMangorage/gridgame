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

    private final BufferedImage image = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
    private final Graphics2D g = image.createGraphics();

    private GameScreen() {
        SERVICE.scheduleAtFixedRate(this::repaint, 0, Math.abs((int) (((double) 1 / 20) * 1000)), TimeUnit.MILLISECONDS);
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
        long start = System.currentTimeMillis();
        super.paintComponent(graphics);

        image.getGraphics().clearRect(0, 0, getWidth(), getHeight());
        setBackground(Color.BLACK);

        //paintExtra(g);
        Events.RENDER_EVENT.trigger(new RenderEvent(g));
        graphics.drawImage(image, 0, 0, null);

        lastUpdateMSLength = System.currentTimeMillis() - start;
    }

    public static void create() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Grid Game! By MangoRage");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GameScreen gameScreen = new GameScreen();
            frame.add(gameScreen);
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setVisible(true);
            frame.addKeyListener(GridGameClient.getInstance());
            frame.addMouseWheelListener(GridGameClient.getInstance());
            frame.addMouseListener(GridGameClient.getInstance());
        });
    }
}
