package ru.nsu.gunko.model.base;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.factory.*;
import ru.nsu.gunko.model.oth.Dealers;
import ru.nsu.gunko.model.oth.controller.*;
import ru.nsu.gunko.model.oth.Suppliers.*;

import java.util.*;

public class Model {
    private ModelListener modelListener;
    private State state;
    private Storages storages;
    private int count;
    private int used;
    private final Map<String, Integer> settings;
    private final Suppliers suppliers;
    private final Factory factory;
    private final Dealers dealers;
    private final Controller controller;

    public Model(Map<String, Integer> map, Storages storages) {
        suppliers = new Suppliers(map, this);
        factory = new Factory(storages, this);
        controller = new Controller(factory, storages, map);
        dealers = new Dealers(map, this);

        settings = map;
        state = State.NOTHING;
    }

    public void start(Map<String, Integer> map, Storages storages) {
        this.storages = storages;

        suppliers.start(storages);
        factory.start(map);
        controller.start();
        dealers.start();
    }

    public void notifyUnsafe(int id) {
        if (modelListener != null) {
            modelListener.onModelChanged(id);
        }
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setModelListener(ModelListener modelListener) {
        this.modelListener = modelListener;
    }

    public State getState() {
        return state;
    }

    public Suppliers getSuppliers() {
        return suppliers;
    }

    public Factory getFactory() {
        return factory;
    }

    public Dealers getDealers() {
        return dealers;
    }

    public Controller getController() {
        return controller;
    }

    public Storages getStorages() {
        return storages;
    }

    public int getCount() {
        return count;
    }

    public int getUsed() {
        return used;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public Map<String, Integer> getSettings() {
        return settings;
    }
}
