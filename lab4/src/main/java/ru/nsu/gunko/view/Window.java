package ru.nsu.gunko.view;

import ru.nsu.gunko.model.base.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

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


        JScrollPane pane = new JScrollPane(textPane);
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));

        pane.setPreferredSize(new Dimension(getPreferredSize().width/4, getPreferredSize().height/2));
        stat.setPreferredSize( new Dimension(getPreferredSize().width/8, getPreferredSize().height/8));

        textPane.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        setResizable(false);
        
        panel.add(pane);
        JPanel secondPart = new JPanel();
        initSecondPart(secondPart);
        panel.add(secondPart);

        getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));

        initStat();
        add(panel);

        SwingUtilities.invokeLater(() -> {
            setSize(getPreferredSize().width/2, getPreferredSize().height/2);
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

    private void initSecondPart(JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel buf = new JPanel(); JPanel buf1 = new JPanel();
        buf.setLayout(new BoxLayout(buf, BoxLayout.Y_AXIS));

        for (int i = 0; i < 4; ++i) {
            JPanel line = new JPanel();
            line.setLayout(new BoxLayout(line, BoxLayout.X_AXIS));
            JSlider slider = new JSlider(0, 100);

            JLabel label = new JLabel();
            label.setText(names.get(i+1));

            slider.setPaintTrack(true);
            slider.setPaintTicks(true);

            slider.setMajorTickSpacing(10);
            slider.setMinorTickSpacing(1);
            line.add(slider); line.add(label);

            buf.add(line);
        }

        JPanel lineOfSell = new JPanel();
        lineOfSell.setLayout(new BoxLayout(lineOfSell, BoxLayout.X_AXIS));
        JSlider slider = new JSlider(0, 100);

        JLabel labelOfSell = new JLabel();
        labelOfSell.setText("Sell");

        slider.setPaintTrack(true);
        slider.setPaintTicks(true);

        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        lineOfSell.add(slider); lineOfSell.add(labelOfSell);

        buf1.add(lineOfSell);

        Dimension dimension = new Dimension(getPreferredSize().width/8, getPreferredSize().height/8);
        buf.setPreferredSize(dimension); buf1.setPreferredSize(dimension);
        panel.add(buf); panel.add(stat); panel.add(buf1);
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
