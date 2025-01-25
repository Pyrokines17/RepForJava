package client;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ProfileDialog extends JDialog {
    private final JLabel usernameLabel;
    private final JTextArea statusField;
    private final JButton avatarButton;
    private String avatarPath;

    public ProfileDialog(JFrame parent, CliCommandManager commandManager) {
        super(parent, "Profile", true);

        usernameLabel = new JLabel();
        statusField = new JTextArea();
        avatarButton = new JButton();
        JButton saveButton = new JButton("Save");

        usernameLabel.setHorizontalAlignment(JLabel.CENTER);
        usernameLabel.setVerticalAlignment(JLabel.CENTER);

        statusField.setLineWrap(true);
        statusField.setWrapStyleWord(true);

        setLayout(new GridLayout(4, 1));

        add(avatarButton);
        add(usernameLabel);
        add(statusField);
        add(saveButton);

        avatarButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                setAvatar(file.getPath());
            }
        });

        saveButton.addActionListener(e -> {
            String username = usernameLabel.getText();
            String status = getStatus();
            String avatarPath = getAvatarPath();

            try {
                if (avatarPath != null) {
                    commandManager.saveProfile(username, status, avatarPath);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select an avatar", "Error", JOptionPane.ERROR_MESSAGE);
                }
                dispose();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        pack();
        setLocationRelativeTo(parent);
    }

    public void setProfile(String username, String status, String avatarPath) {
        usernameLabel.setText(username);
        statusField.setText(status);
        setAvatar(avatarPath);
        this.avatarPath = avatarPath;
    }

    private void setAvatar(String avatarPath) {
        ImageIcon imageIcon = new ImageIcon(
                new ImageIcon(avatarPath).getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT));
        avatarButton.setIcon(imageIcon);
        this.avatarPath = avatarPath;
    }

    public String getStatus() {
        return statusField.getText();
    }

    public String getAvatarPath() {
        return avatarPath;
    }
}
