package ru.nsu.gunko.view;

import ru.nsu.gunko.model.base.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

public class Window extends JFrame implements ModelListener {
    private final Map<Integer, JTextPane> map;
    private final Map<Integer, String> names;
    private final JTextPane textPane;
    private final JPanel stat;
    private boolean flag;
    private Model model;

    public Window() {
        super("Factory");
        map = new HashMap<>();
        names = new HashMap<>();
        flag = true;
        initNames();

        JPanel panel = new JPanel();
        textPane = new JTextPane();
        stat = new JPanel();
        JScrollPane pane = new JScrollPane(panel);

        textPane.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
        setExtendedState(MAXIMIZED_BOTH);

        panel.add(textPane);
        panel.add(stat);
        initStat();
        add(pane);

        SwingUtilities.invokeLater(() -> {
            setSize(400, 300);
            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    flag = false;
                }
            });
            setVisible(true);
        });
    }

    @Override
    public void onModelChanged(int id) {
        switch (model.getState()) {
            case WRITE_SELL: {
                String newString = textPane.getText() + model.getDealers().getSell().getString() + "\n";
                textPane.setText(newString);
                model.setState(State.NOTHING);
                break;
            }
            case CHANGE_STAT: {
                map.get(id).setText("Count/Used of "+names.get(id)+model.getCount()+"/"+model.getUsed());
                model.setState(State.NOTHING);
                break;
            }
            case NOTHING: {
                break;
            }
        }
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public boolean getFlag() {
        return flag;
    }

    private void initNames() {
        names.put(1, "body: ");
        names.put(2, "motor: ");
        names.put(3, "accessory: ");
        names.put(4, "car: ");
    }

    private void initStat() {
        stat.setLayout(new BoxLayout(stat, BoxLayout.Y_AXIS));

        for (int i = 0; i < 4; ++i) {
            JTextPane pane = new JTextPane();
            map.put(i+1, pane);
            stat.add(pane);
        }
    }
}
