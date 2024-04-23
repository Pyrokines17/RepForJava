package ru.nsu.gunko.view;

import ru.nsu.gunko.controller.*;
import ru.nsu.gunko.model.base.*;
import ru.nsu.gunko.model.factory.*;
import ru.nsu.gunko.model.oth.Suppliers.Puts;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.util.List;


public class Window extends JFrame implements ModelListener {
    private final List<SwingController> controllers;
    private final Map<Integer, JLabel> map;
    private final Map<Integer, String> names;
    private final List<JLabel> labels;

    private final JTextPane textPane;
    private final JPanel secondPart;
    private final JPanel stat;

    private boolean flag;
    private Model model;

    public Window() {
        super("Factory");
        controllers = new ArrayList<>();
        map = new HashMap<>();
        names = new HashMap<>();
        flag = true;
        initNames();
        labels = new ArrayList<>();

        JPanel panel = new JPanel();
        textPane = new JTextPane();
        stat = new JPanel();
        JScrollPane pane = new JScrollPane(textPane);

        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        pane.setPreferredSize(new Dimension(getPreferredSize().width/4, getPreferredSize().height/3));

        textPane.getDocument().putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        setResizable(false);

        pane.getVerticalScrollBar().addAdjustmentListener(e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum()));
        
        panel.add(pane);
        secondPart = new JPanel();
        panel.add(secondPart);

        secondPart.setPreferredSize(new Dimension(getPreferredSize().width/4, getPreferredSize().height/3));
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

                    for (SwingController controller : controllers) {
                        controller.setFlag(false);
                    }
                }
            });

            setLocationRelativeTo(null);
            setVisible(true);
        });
    }

    @Override
    public void onModelChanged() {
        switch (model.getState()) {
            case WRITE_SELL: {
                SwingUtilities.invokeLater(() -> {
                    String newString = textPane.getText() + model.getDealers().getSell().getString() + "\n";
                    textPane.setText(newString);
                });
                model.setState(State.NOTHING);
                break;
            }
            case CHANGE_STAT: {
                SwingUtilities.invokeLater(() -> {
                    Puts puts = model.getSuppliers().getPuts();
                    Assembly assembly = model.getFactory().getAssembly();
                    map.get(1).setText("Count/Used of "+names.get(1)+model.getStorages().bodyStorage().size()+"/"+puts.bodyPut().getCount());
                    map.get(2).setText("Count/Used of "+names.get(2)+model.getStorages().motorStorage().size()+"/"+puts.motorPut().getCount());
                    map.get(3).setText("Count/Used of "+names.get(3)+model.getStorages().accessoryStorage().size()+"/"+puts.accessoryPut().getCount());
                    map.get(4).setText("Count/Used of "+names.get(4)+assembly.getSize()+"/"+assembly.getCount());
                });
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
        initSecondPart();
    }

    public boolean getFlag() {
        return flag;
    }

    private void initNames() {
        names.put(0, "sell: ");
        names.put(1, "body: ");
        names.put(2, "motor: ");
        names.put(3, "accessory: ");
        names.put(4, "car: ");
    }

    private void initSecondPart() {
        secondPart.setLayout(new BoxLayout(secondPart, BoxLayout.Y_AXIS));

        stat.add(new JLabel("\n"));

        for (int i = 0; i < 4; ++i) {
            JPanel line = new JPanel();
            line.setLayout(new BoxLayout(line, BoxLayout.X_AXIS));
            JSlider slider = new JSlider(0, 100);

            JLabel label = new JLabel();
            label.setText(names.get(i));

            SwingController curControl = new SwingController(model, i, slider, labels);
            slider.addChangeListener(curControl);
            controllers.add(curControl);

            slider.setPaintTrack(true);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);

            slider.setMajorTickSpacing(10);
            slider.setMinorTickSpacing(1);
            line.add(label); line.add(slider);

            stat.add(line);

            JLabel jLabel = new JLabel("Speed: 50%");
            labels.add(jLabel); line.add(jLabel);

            stat.add(new JLabel("\n"));
        }

        secondPart.add(stat);
    }

    private void initStat() {
        stat.setLayout(new BoxLayout(stat, BoxLayout.Y_AXIS));
        
        for (int i = 0; i < 4; ++i) {
            JLabel pane = new JLabel();
            map.put(i+1, pane);
            stat.add(pane);
        }
    }
}
