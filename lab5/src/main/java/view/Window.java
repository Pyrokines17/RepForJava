package view;

import client.*;
import controller.*;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.plaf.basic.*;

public class Window extends JFrame {
    private final CliCommandManager commandManager;
    private final Dimension screenSize;
    private JTextArea chatHistory;

    private final String ip;
    private final int port;

    public Window(CliCommandManager commandManager, Controller controller) {
        setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.commandManager = commandManager;
        setTitle("Chat");

        setSize(screenSize.width / 2, screenSize.height / 2);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addComponents();

        SettingsDialog settingsDialog = new SettingsDialog(this);
        settingsDialog.setSize(screenSize.width/8, screenSize.height/8);
        settingsDialog.setResizable(false);
        settingsDialog.setVisible(true);

        ip = settingsDialog.getIp();
        port = settingsDialog.getPort();

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    controller.setCommand("exit");
                }
            }
        });
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    private void addComponents() {
        chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        chatHistory.setLineWrap(true);
        chatHistory.setWrapStyleWord(true);

        JScrollPane chatHistoryScrollPane = new JScrollPane(chatHistory);
        chatHistoryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JTextArea userInput = new JTextArea();
        userInput.setLineWrap(true);
        userInput.setWrapStyleWord(true);
        JButton sendButton = getSendButton(userInput);

        JPanel southPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        southPanel.add(new JScrollPane(userInput), constraints);

        constraints.weightx = 0;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        southPanel.add(sendButton, constraints);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chatHistoryScrollPane, southPanel);
        splitPane.setResizeWeight(0.9);
        ((BasicSplitPaneUI)splitPane.getUI()).getDivider().setEnabled(false);

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1));

        buttonsPanel.add(getLoginButton());
        buttonsPanel.add(getListButton());
        buttonsPanel.add(getLogoutButton());

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.EAST);
    }

    private JButton getLoginButton() {
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            LoginDialog loginDialog = new LoginDialog(this);
            loginDialog.setSize(screenSize.width/8, screenSize.height/8);
            loginDialog.setResizable(false);
            loginDialog.setVisible(true);
        });

        return loginButton;
    }

    private JButton getSendButton(JTextArea userInput) {
        JButton sendButton = new JButton("Send");

        ActionListener sendMessageAction = e -> {
            String message = userInput.getText();
            if (!message.trim().isEmpty()) {
                try {
                    commandManager.clientMes(message);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                userInput.setText("");
            }
        };

        sendButton.addActionListener(sendMessageAction);
        return sendButton;
    }

    private JButton getLogoutButton() {
        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> {
            try {
                commandManager.logout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return logoutButton;
    }

    private JButton getListButton() {
        JButton listButton = new JButton("List");

        listButton.addActionListener(e -> {
            try {
                commandManager.list();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return listButton;
    }

    public CliCommandManager getCommandManager() {
        return commandManager;
    }

    public void updateChat(String message) {
        chatHistory.append(message + "\n");
    }
}
