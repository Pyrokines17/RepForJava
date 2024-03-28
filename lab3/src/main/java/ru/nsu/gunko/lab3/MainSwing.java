package ru.nsu.gunko.lab3;

import ru.nsu.gunko.lab3.controllerSwing.ControllerSwing;
import ru.nsu.gunko.lab3.model.Model;
import ru.nsu.gunko.lab3.viewSwing.ViewSwing;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainSwing {

    public static void main(String[] args) {
        Model model = new Model();
        ViewSwing viewSwing = new ViewSwing(model);
        ControllerSwing controllerSwing = new ControllerSwing(model);

        controllerSwing.playMusic();

        viewSwing.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                controllerSwing.entry(e);
            }
        });

        SwingUtilities.invokeLater(() -> viewSwing.setVisible(true));
    }

}
