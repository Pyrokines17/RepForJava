package view;

import javax.swing.*;
import java.awt.*;

public class UserProfile extends JDialog {
    private final JLabel usernameLabel;
    private final JTextArea statusField;
    private final JLabel avatarLabel;

    public UserProfile(JFrame parent) {
        super(parent, "User Profile", true);

        usernameLabel = new JLabel();
        statusField = new JTextArea();
        avatarLabel = new JLabel();

        usernameLabel.setHorizontalAlignment(JLabel.CENTER);
        usernameLabel.setVerticalAlignment(JLabel.CENTER);

        avatarLabel.setHorizontalAlignment(JLabel.CENTER);
        avatarLabel.setVerticalAlignment(JLabel.CENTER);

        statusField.setLineWrap(true);
        statusField.setWrapStyleWord(true);
        statusField.setEditable(false);

        setLayout(new GridLayout(3, 1));

        add(avatarLabel);
        add(usernameLabel);
        add(statusField);

        pack();
        setLocationRelativeTo(parent);
    }

    public void setUserProfile(String username, String status, ImageIcon avatar) {
        usernameLabel.setText(username);
        statusField.setText(status);
        avatarLabel.setIcon(avatar);
    }
}
