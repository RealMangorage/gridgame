package org.mangorage.gridgame.render;

import org.mangorage.gridgame.game.Game;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class RenderableScreen extends JPanel {

    private RenderableScreen() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, Math.abs((int) (((double) 1 / 20) * 1000)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Set background color
        setBackground(Color.BLACK);
        Game.getInstance().render(g);
    }

    public static void create() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Renderable Screen");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            RenderableScreen renderableScreen = new RenderableScreen();
            frame.add(renderableScreen);
            frame.setSize(400, 400);
            frame.setVisible(true);
            frame.addKeyListener(Game.getInstance());
            frame.addMouseWheelListener(Game.getInstance());
        });
    }
}