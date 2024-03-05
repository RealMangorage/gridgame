package org.mangorage.gridgame.client.screen;


import org.mangorage.gridgame.client.GridGameClient;
import org.mangorage.gridgame.common.Events;
import org.mangorage.gridgame.common.events.RenderEvent;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RenderableScreen extends JPanel {
    public static long lastUpdateMSLength = 0;
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private static final DecimalFormat formatter = new DecimalFormat("#,###");

    private final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor((r) -> new Thread(r, "Render-Thread"));

    private RenderableScreen() {
        SERVICE.scheduleAtFixedRate(this::repaint, 0, Math.abs((int) (((double) 1 / 20) * 1000)), TimeUnit.MILLISECONDS);
    }

    @Override
    protected void paintComponent(Graphics g) {
        long start = System.currentTimeMillis();
        super.paintComponent(g);
        setBackground(Color.BLACK);

        Events.RENDER_EVENT.trigger(new RenderEvent(g));

        lastUpdateMSLength = System.currentTimeMillis() - start;
    }

    public static void create() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Grid Game! By MangoRage");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            RenderableScreen renderableScreen = new RenderableScreen();
            frame.add(renderableScreen);
            frame.setSize(400, 400);
            frame.setVisible(true);
            frame.addKeyListener(GridGameClient.getInstance());
            frame.addMouseWheelListener(GridGameClient.getInstance());
            frame.addMouseListener(GridGameClient.getInstance());
        });
    }
}
