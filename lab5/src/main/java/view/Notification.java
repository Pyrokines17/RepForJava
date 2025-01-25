package view;

import javax.swing.*;

public class Notification {
    public static void show(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
