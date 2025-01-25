package ru.nsu.gunko.lab3.viewSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class RockPSwing extends JComponent implements PersonSwing {
    private final Dimension window;
    private boolean flag;
    private int x, y;

    public RockPSwing(JLayeredPane stackPane, Dimension window) {
        this.window = window;
        this.setSize(window);
        stackPane.add(this);
        flag = true;
    }

    @Override
    public void moveP(int x, int y) {
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
        int height = 120, width = 120;
        Rectangle2D.Double circle = new Rectangle2D.Double(x, y, width, height);
        g.translate(Double.valueOf(window.getWidth()/2).intValue(),
                Double.valueOf(window.getHeight()/2).intValue());
        Graphics2D rock = (Graphics2D) g;
        rock.setColor(Color.BLACK);
        rock.fill(circle);
    }
}
