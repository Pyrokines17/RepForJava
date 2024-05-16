package view;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginDialog extends JDialog {

    public LoginDialog(Window window) {
        super(window, "Login", ModalityType.APPLICATION_MODAL);
        setLocationRelativeTo(window);

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Try: "));
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(e -> {
            try {
                window.getCommandManager().login(usernameField.getText(), new String(passwordField.getPassword()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            setVisible(false);
        });

        panel.add(submitButton);

        add(panel);
    }
}
