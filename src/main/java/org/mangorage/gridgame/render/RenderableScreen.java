package org.mangorage.gridgame.render;

import org.mangorage.gridgame.game.Game;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class RenderableScreen extends JPanel {
    public static long lastUpdateMSLength = 0;
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private static final DecimalFormat formatter = new DecimalFormat("#,###");

    private RenderableScreen() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, Math.abs((int) (((double) 1 / 20) * 1000)));
    }

    private void renderDebugStats(Graphics graphics) {
        var rate = Game.getInstance().getTickRate();
        var lastMS = Game.getInstance().getLastMSPerTick();
        var expectedMS = ((double) 1 / rate) * 1000;
        var currentTPS = Math.min(rate, 1000.0 / lastMS);

        var lastSave = Game.getInstance().getSaveStartTime();
        var length = Game.getInstance().getSaveLenghth();
        var saving = Game.getInstance().isSaving();

        var ticked = Game.getInstance().getGrid().getTickedTE();

        graphics.setColor(Color.WHITE);
        ((Graphics2D) graphics).scale(2, 2);
        graphics.drawString("Expected %stps (%sms/t)".formatted(rate, expectedMS), 10, 10);
        graphics.drawString("Current %stps (%sms/t)".formatted(df.format(currentTPS), lastMS), 10, 20);
        graphics.drawString("Save Status: %s (%s ms)".formatted(saving, saving ? System.currentTimeMillis() - lastSave : length), 10, 30);
        graphics.drawString("Ticked: %s".formatted(formatter.format(ticked)), 10, 40);
    }

    @Override
    protected void paintComponent(Graphics g) {
        long start = System.currentTimeMillis();
        super.paintComponent(g);
        setBackground(Color.BLACK);
        Game.getInstance().render(g);
        if (Game.getInstance().showDebug())
            renderDebugStats(g);
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
            frame.addKeyListener(Game.getInstance());
            frame.addMouseWheelListener(Game.getInstance());
            frame.addMouseListener(Game.getInstance());
        });
    }
}