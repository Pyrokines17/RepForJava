package view;

import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {
    private final JTextField ipField;
    private final JTextField portField;
    private String ip;
    private int port;

    public SettingsDialog(Frame parent) {
        super(parent, "Settings", true);
        setLayout(new GridLayout(3, 2));

        JLabel ipLabel = new JLabel("IP:");
        ipField = new JTextField();

        JLabel portLabel = new JLabel("Port:");
        portField = new JTextField();

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            ip = ipField.getText();
            try {
                port = Integer.parseInt(portField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Port must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            }
            setVisible(false);
        });

        add(ipLabel);
        add(ipField);
        add(portLabel);
        add(portField);
        add(new JLabel("Confirm:"));
        add(okButton);

        pack();
        setLocationRelativeTo(parent);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}