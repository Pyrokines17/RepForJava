package view;

import client.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import controller.*;
import javax.swing.*;
import java.awt.event.*;
import java.nio.file.Path;
import javax.swing.plaf.basic.*;

public class Window extends JFrame {
    private final Map<String, String> filesID;

    private final DefaultListModel<String> usersModel;
    private final JList<String> usersList;

    private final DefaultListModel<String> filesModel;
    private final JList<String> filesList;

    private final CliCommandManager commandManager;
    private final Dimension screenSize;
    private JTextArea chatHistory;

    private final String ip;
    private final int port;

    private Listener listener;

    public Window(CliCommandManager commandManager, Controller controller) {
        setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.commandManager = commandManager;
        setTitle("Chat");

        usersModel = new DefaultListModel<>();
        usersList = new JList<>(usersModel);
        filesModel = new DefaultListModel<>();
        filesList = new JList<>(filesModel);

        filesID = new HashMap<>();

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
        buttonsPanel.add(getSendButton());
        buttonsPanel.add(getDownloadButton());

        JPanel westPanel = new JPanel(new GridLayout(2, 1));
        westPanel.add(new JScrollPane(usersList));
        westPanel.add(new JScrollPane(filesList));

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.EAST);
        add(westPanel, BorderLayout.WEST);
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
                usersModel.clear();
                chatHistory.setText("");
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

    private JButton getSendButton() {
        JButton sendFileButton = new JButton("Send File");

        sendFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                Path path = selectedFile.toPath();

                try {
                    commandManager.sendFile(path.toString());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        return sendFileButton;
    }

    private JButton getDownloadButton() {
        JButton downloadButton = new JButton("Download File");

        downloadButton.addActionListener(e -> {
            String fileName = filesList.getSelectedValue();

            if (fileName != null) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedDirectory = fileChooser.getSelectedFile();
                    Path path = selectedDirectory.toPath();

                    try {
                        listener.setPath(path.toString());
                        commandManager.download(filesID.get(fileName));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No file selected from the list", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return downloadButton;
    }

    public void updateUsers(String user, boolean add) {
        if (add) {
            usersModel.addElement(user);
        } else {
            usersModel.removeElement(user);
        }
    }

    public CliCommandManager getCommandManager() {
        return commandManager;
    }

    public void updateChat(String message) {
        chatHistory.append(message + "\n");
    }

    public void updateFiles(String file) {
        String key;

        String[] parts = file.split(";");
        String[] subParts = parts[0].split(" ");
        String[] subParts1 = parts[2].split(" ");
        key = subParts[2]+"-"+subParts1[2]+"b";

        filesID.put(key, parts[4].split(" ")[2]);

        filesModel.addElement(key);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
