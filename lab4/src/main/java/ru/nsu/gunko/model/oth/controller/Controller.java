package ru.nsu.gunko.model.oth.controller;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.factory.Factory;

import java.util.Map;
import java.util.concurrent.*;

public class Controller {
    private final Storages storages;
    private final Map<String, Integer> map;
    private final Request request;

    public Controller(Factory factory, Storages storages, Map<String, Integer> map) {
        this.storages = storages;
        this.map = map;
        request = new Request(factory);
    }

    public void start() {
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(request);
    }

    public void signal() {
        if (storages.carStorage().size() != map.get(Config.AUTO_SIZE.name())) {
            request.setSignal(true);
        }
    }

    public void finish() {
        request.setFlag(false);
    }
}
