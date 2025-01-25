package view;

import client.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import controller.*;
import javax.swing.*;
import java.awt.event.*;
import java.nio.file.Path;
import com.vdurmont.emoji.*;
import javax.swing.plaf.basic.*;

public class Window extends JFrame {
    private final Map<String, String> filesID;
    private final CustomListCell customListCell;

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
    private String userName;

    public Window(CliCommandManager commandManager, Controller controller) {
        setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.commandManager = commandManager;
        setTitle("Chat");

        usersModel = new DefaultListModel<>();
        usersList = new JList<>(usersModel);
        filesModel = new DefaultListModel<>();
        filesList = new JList<>(filesModel);

        customListCell = new CustomListCell();
        filesList.setCellRenderer(customListCell);
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
        setResizable(true);
        setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    controller.setCommand("exit");
                }
            }
        });
    }

    public void initFiles() {
        if (!filesModel.contains("Files: ")) {
            filesModel.addElement("Files: ");
        }
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(new JScrollPane(userInput), BorderLayout.CENTER);
        southPanel.add(sendButton, BorderLayout.EAST);

        JButton emojiButton = getEmojiButton(userInput);
        southPanel.add(emojiButton, BorderLayout.WEST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chatHistoryScrollPane, southPanel);
        splitPane.setResizeWeight(0.9);
        ((BasicSplitPaneUI)splitPane.getUI()).getDivider().setEnabled(false);

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 2));

        buttonsPanel.add(getLoginButton());
        buttonsPanel.add(getListButton());
        buttonsPanel.add(getLogoutButton());
        buttonsPanel.add(getSendButton());
        buttonsPanel.add(getDownloadButton());
        buttonsPanel.add(getProfileButton());

        JPanel westPanel = new JPanel(new GridLayout(2, 1));

        westPanel.add(new JScrollPane(usersList));
        westPanel.add(new JScrollPane(filesList));

        JPanel settingsPanel = cusomizeUI();

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.EAST);
        add(westPanel, BorderLayout.WEST);
        add(settingsPanel, BorderLayout.NORTH);
    }

    private JPanel cusomizeUI() {
        JPanel settingsPanel = new JPanel();

        JComboBox<String> fontStylesComboBox = getStringJComboBox();
        settingsPanel.add(new JLabel("Font Style: "));
        settingsPanel.add(fontStylesComboBox);

        Integer[] fontSizes = {10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30};
        JComboBox<Integer> fontSizesComboBox = new JComboBox<>(fontSizes);
        fontSizesComboBox.addActionListener(e -> {
            Integer selectedFontSize = (Integer)fontSizesComboBox.getSelectedItem();
            Font font = chatHistory.getFont();

            if (selectedFontSize != null) {
                chatHistory.setFont(font.deriveFont((float)selectedFontSize));
            }
        });

        settingsPanel.add(new JLabel("Font Size: "));
        settingsPanel.add(fontSizesComboBox);

        JComboBox<String> fontColorsComboBox = getjComboBox();
        settingsPanel.add(new JLabel("Font Color: "));
        settingsPanel.add(fontColorsComboBox);

        JButton backgroundButton = new JButton("Background Color");
        backgroundButton.addActionListener(e -> {
            Color backgroundColor = JColorChooser.showDialog(null, "Choose Background Color", chatHistory.getBackground());
            chatHistory.setBackground(backgroundColor);
        });

        settingsPanel.add(backgroundButton);

        return settingsPanel;
    }

    private JButton getEmojiButton(JTextArea userInput) {
        JButton emojiButton = new JButton("Emoji");
        String[] emojis = {"\uD83D\uDE00", "\uD83D\uDE01", "\uD83D\uDE02", "\uD83D\uDE03", "\uD83D\uDE04"};

        emojiButton.addActionListener(e -> {
            String selectedEmoji = (String)JOptionPane.showInputDialog(
                    null,
                    "Choose an emoji",
                    "Emoji",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    emojis,
                    emojis[0]);

            if (selectedEmoji != null) {
                userInput.append(selectedEmoji);
            }
        });

        return emojiButton;
    }

    private JComboBox<String> getjComboBox() {
        String[] fontColors = {"Black", "Red", "Green", "Blue"};
        JComboBox<String> fontColorsComboBox = new JComboBox<>(fontColors);
        fontColorsComboBox.addActionListener(e -> {
            String selectedFontColor = (String)fontColorsComboBox.getSelectedItem();

            switch (selectedFontColor) {
                case "Black":
                    chatHistory.setForeground(Color.BLACK);
                    break;
                case "Red":
                    chatHistory.setForeground(Color.RED);
                    break;
                case "Green":
                    chatHistory.setForeground(Color.GREEN);
                    break;
                case "Blue":
                    chatHistory.setForeground(Color.BLUE);
                    break;
                case null:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + selectedFontColor);
            }
        });
        return fontColorsComboBox;
    }

    private JComboBox<String> getStringJComboBox() {
        String[] fontStyles = {"Normal", "Bold", "Italic", "Bold Italic"};
        JComboBox<String> fontStylesComboBox = new JComboBox<>(fontStyles);
        fontStylesComboBox.addActionListener(e -> {
            String selectedFontStyle = (String)fontStylesComboBox.getSelectedItem();
            Font font = chatHistory.getFont();

            switch (selectedFontStyle) {
                case "Normal":
                    chatHistory.setFont(font.deriveFont(Font.PLAIN));
                    break;
                case "Bold":
                    chatHistory.setFont(font.deriveFont(Font.BOLD));
                    break;
                case "Italic":
                    chatHistory.setFont(font.deriveFont(Font.ITALIC));
                    break;
                case "Bold Italic":
                    chatHistory.setFont(font.deriveFont(Font.BOLD | Font.ITALIC));
                    break;
                case null:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + selectedFontStyle);
            }
        });
        return fontStylesComboBox;
    }

    private JButton getLoginButton() {
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            LoginDialog loginDialog = new LoginDialog(this);
            loginDialog.setSize(screenSize.width/8, screenSize.height/8);
            loginDialog.setResizable(false);
            loginDialog.setVisible(true);

            try {
                commandManager.list();
                commandManager.fileList();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return loginButton;
    }

    private JButton getSendButton(JTextArea userInput) {
        JButton sendButton = new JButton("Send");

        ActionListener sendMessageAction = e -> {
            String message = userInput.getText();
            if (!message.trim().isEmpty()) {
                try {
                    String messageWithEmoji = EmojiParser.parseToAliases(message);
                    commandManager.clientMes(messageWithEmoji);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                userInput.setText("");
            }
        };

        userInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
                    sendMessageAction.actionPerformed(null);
                }
            }
        });

        sendButton.addActionListener(sendMessageAction);
        return sendButton;
    }

    private JButton getLogoutButton() {
        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> {
            try {
                commandManager.logout();
                usersModel.clear();
                filesModel.clear();
                chatHistory.setText("");
                userName = null;
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

    private JButton getProfileButton() {
        JButton profileButton = new JButton("Profile");

        profileButton.addActionListener(e -> {
            String name = usersList.getSelectedValue();

            if (name != null) {
                if (name.equals(userName)) {
                    ProfileDialog profileDialog = new ProfileDialog(this, commandManager);
                    profileDialog.setProfile(name, "", "src/main/resources/role-model.png");
                    profileDialog.setSize(screenSize.width/8, screenSize.height/4);
                    profileDialog.setVisible(true);
                } else {
                    try {
                        commandManager.showProfile(name);
                    } catch (IOException ee) {
                        throw new RuntimeException(ee);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No name selected from the list", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return profileButton;
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
            if (!usersModel.contains(user)) {
                usersModel.addElement(user);
            }
        } else {
            if (usersModel.contains(user)) {
                usersModel.removeElement(user);
            }
        }
    }

    public CliCommandManager getCommandManager() {
        return commandManager;
    }

    public void updateChat(String message) {
        String messageWithEmoji = EmojiParser.parseToUnicode(message);
        chatHistory.append(messageWithEmoji + "\n");
    }

    public void updateFiles(String file) {
        String key;

        String[] parts = file.split(";");
        String[] subParts = parts[0].split(" ");
        String[] subParts1 = parts[2].split(" ");
        key = subParts[2]+"-"+subParts1[2]+"b";

        filesID.put(key, parts[4].split(" ")[2]);
        customListCell.filesInfo.put(key, file);
        filesModel.addElement(key);
    }

    public Dimension getScreenSize() {
        return screenSize;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Map<String, String> getFilesInfo() {
        return customListCell.getFilesInfo();
    }

    public void clearUsers() {
        usersModel.clear();
    }

    private static class CustomListCell extends DefaultListCellRenderer {
        private final Map<String, String> filesInfo;

        public CustomListCell() {
            filesInfo = new HashMap<>();
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String key = (String)value;
            String info = filesInfo.get(key);
            label.setToolTipText(info);
            return label;
        }

        public Map<String, String> getFilesInfo() {
            return filesInfo;
        }
    }
}
