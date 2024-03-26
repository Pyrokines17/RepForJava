package ru.nsu.gunko.lab3.viewSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SkeletonPSwing extends JComponent implements PersonSwing {
    private final Dimension window;
    private boolean flag;
    int x, y;

    public SkeletonPSwing(JLayeredPane stackPane, Dimension window) {
        this.window = window;
        this.setSize(window);
        stackPane.add(this);
        flag = true;
    }

    @Override
    public void move(int x, int y) {
        if (flag) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void deleteImage(JLayeredPane stackPane) {
        flag = false;
        x = 99999;
        y = 99999;
    }

    @Override
    public void paintComponent(Graphics g) {
        int height = 90, width = 90;
        Rectangle2D.Double circle = new Rectangle2D.Double(x, y, width, height);
        g.translate(Double.valueOf(window.getWidth()/2).intValue(),
                Double.valueOf(window.getHeight()/2).intValue());
        Graphics2D enemy = (Graphics2D) g;
        enemy.setColor(Color.BLUE);
        enemy.fill(circle);
    }
}
