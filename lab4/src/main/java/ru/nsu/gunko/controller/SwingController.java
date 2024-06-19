package ru.nsu.gunko.controller;

import ru.nsu.gunko.model.base.*;

import javax.swing.*;
import javax.swing.event.*;
import java.util.List;

public class SwingController implements ChangeListener {
    private final JSlider slider;
    private final Model model;
    private final List<JLabel> labels;

    private final int id;
    private boolean flag;

    public SwingController(Model model, int id, JSlider slider, List<JLabel> labels) {
        this.model = model;
        this.id = id;
        this.slider = slider;
        this.flag = true;
        this.labels = labels;
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        double SPEED = 200;
        double bufValue = 10000;
        double value = SPEED*slider.getValue()/100;

        if (flag && value!=0) {
            switch (id) {
                case 0: {
                    model.getDealers().getSell().setTime(bufValue / value);
                    labels.get(id).setText("Speed: "+slider.getValue()+"%");
                    break;
                }
                case 1: {
                    model.getSuppliers().getPuts().bodyPut().setTime(bufValue / value);
                    labels.get(id).setText("Speed: "+slider.getValue()+"%");
                    break;
                }
                case 2: {
                    model.getSuppliers().getPuts().motorPut().setTime(bufValue / value);
                    labels.get(id).setText("Speed: "+slider.getValue()+"%");
                    break;
                }
                case 3: {
                    model.getSuppliers().getPuts().accessoryPut().setTime(bufValue / value);
                    labels.get(id).setText("Speed: "+slider.getValue()+"%");
                    break;
                }
            }
        }
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
