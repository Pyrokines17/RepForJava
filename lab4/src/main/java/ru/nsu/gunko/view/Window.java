package ru.nsu.gunko.view;

import ru.nsu.gunko.controller.*;
import ru.nsu.gunko.model.base.*;
import ru.nsu.gunko.model.factory.*;
import ru.nsu.gunko.model.parts.body.*;
import ru.nsu.gunko.model.parts.motor.*;
import ru.nsu.gunko.model.parts.accessory.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

public class Window extends JFrame implements ModelListener {
    private final Map<Integer, JLabel> map;
    private final Map<Integer, String> names;
    private final JTextPane textPane;
    private final JPanel secondPart;
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
                }
            });

            setLocationRelativeTo(null);
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
                SwingUtilities.invokeLater(() -> {
                    switch (id) {
                        case 1: {
                            BodyPut put = model.getSuppliers().getPuts().bodyPut();
                            map.get(id).setText("Count/Used of "+names.get(id)+put.getSize()+"/"+put.getCount());
                            break;
                        }
                        case 2: {
                            MotorPut put = model.getSuppliers().getPuts().motorPut();
                            map.get(id).setText("Count/Used of "+names.get(id)+put.getSize()+"/"+put.getCount());
                            break;
                        }
                        case 3: {
                            AccessoryPut put = model.getSuppliers().getPuts().accessoryPut();
                            map.get(id).setText("Count/Used of "+names.get(id)+put.getSize()+"/"+put.getCount());
                            break;
                        }
                        case 4: {
                            Assembly assembly = model.getFactory().getAssembly();
                            map.get(id).setText("Count/Used of "+names.get(id)+assembly.getSize()+"/"+assembly.getCount());
                            break;
                        }
                    }
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

        for (int i = 0; i < 4; ++i) {
            JPanel line = new JPanel();
            line.setLayout(new BoxLayout(line, BoxLayout.X_AXIS));
            JSlider slider = new JSlider(1, 100);

            JLabel label = new JLabel();
            label.setText(names.get(i));
            slider.addChangeListener(new SwingController(model, i, slider));

            slider.setPaintTrack(true);
            slider.setPaintTicks(true);

            slider.setMajorTickSpacing(10);
            slider.setMinorTickSpacing(1);
            line.add(label); line.add(slider);

            stat.add(line);
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
