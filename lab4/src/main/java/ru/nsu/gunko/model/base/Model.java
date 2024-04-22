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

    public synchronized void notifyUnsafe(int id) {
        if (modelListener != null) {
            modelListener.onModelChanged(id);
        }
    }

    public synchronized void setState(State state) {
        this.state = state;
    }

    public synchronized void setModelListener(ModelListener modelListener) {
        this.modelListener = modelListener;
    }

    public synchronized State getState() {
        return state;
    }

    public synchronized Suppliers getSuppliers() {
        return suppliers;
    }

    public synchronized Factory getFactory() {
        return factory;
    }

    public synchronized Dealers getDealers() {
        return dealers;
    }

    public synchronized Controller getController() {
        return controller;
    }

    public synchronized Storages getStorages() {
        return storages;
    }

    public synchronized Map<String, Integer> getSettings() {
        return settings;
    }
}
