package ru.nsu.gunko.controller;

import ru.nsu.gunko.model.base.*;

import javax.swing.*;
import javax.swing.event.*;

public class SwingController implements ChangeListener {
    private final JSlider slider;
    private final Model model;
    private final int id;
    private boolean flag;

    public SwingController(Model model, int id, JSlider slider) {
        this.model = model;
        this.id = id;
        this.slider = slider;
        this.flag = true;
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        double SPEED = 400;
        double bufValue = 10000;
        double value = SPEED*slider.getValue()/100;

        if (flag) {
            switch (id) {
                case 0: {
                    model.getDealers().getSell().setTime(bufValue / value);
                    break;
                }
                case 1: {
                    model.getSuppliers().getPuts().bodyPut().setTime(bufValue / value);
                    break;
                }
                case 2: {
                    model.getSuppliers().getPuts().motorPut().setTime(bufValue / value);
                    break;
                }
                case 3: {
                    model.getSuppliers().getPuts().accessoryPut().setTime(bufValue / value);
                    break;
                }
            }
        }
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
