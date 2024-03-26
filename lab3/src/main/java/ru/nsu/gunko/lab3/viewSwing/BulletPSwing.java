package ru.nsu.gunko.lab3.viewSwing;

import ru.nsu.gunko.lab3.model.Side;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BulletPSwing extends JComponent implements PersonSwing {
    private final Dimension window;
    private boolean flag;
    private int x, y;

    public BulletPSwing(JLayeredPane stackPane, Side ignoredSide, Dimension window) {
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
        int height = 60, width = 60;
        Ellipse2D.Double circle = new Ellipse2D.Double(x, y, width, height);
        g.translate(Double.valueOf(window.getWidth()/2).intValue(),
                Double.valueOf(window.getHeight()/2).intValue());
        Graphics2D bullet = (Graphics2D) g;
        bullet.setColor(Color.RED);
        bullet.fill(circle);
    }
}
