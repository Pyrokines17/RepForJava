package ru.nsu.gunko.controller;

import ru.nsu.gunko.model.base.*;

import javax.swing.*;
import javax.swing.event.*;

public class SwingController implements ChangeListener {
    private final JSlider slider;
    private final Model model;
    private final int id;

    public SwingController(Model model, int id, JSlider slider) {
        this.model = model;
        this.id = id;
        this.slider = slider;
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        double SPEED = 400;
        double bufValue = 400;
        double value = SPEED*slider.getValue()/100;

        switch (id) {
            case 0: {
                model.getDealers().getSell().setTime(bufValue/value);
                break;
            }
            case 1: {
                model.getSuppliers().getPuts().bodyPut().setTime(bufValue/value);
                break;
            }
            case 2: {
                model.getSuppliers().getPuts().motorPut().setTime(bufValue/value);
                break;
            }
            case 3: {
                model.getSuppliers().getPuts().accessoryPut().setTime(bufValue/value);
                break;
            }
        }
    }
}
