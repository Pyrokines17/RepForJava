package ru.nsu.gunko.lab3.controllerSwing;


import javax.swing.*;

public class PlatformHelperSwing {
    public static void run (Runnable treatment) {
        if (treatment == null) {
            throw new IllegalArgumentException("The treatment to perform can not be null");
        }

        SwingUtilities.invokeLater(treatment);
    }
}
