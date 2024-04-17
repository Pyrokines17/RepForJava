package ru.nsu.gunko.model.oth.controller;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.factory.*;

import java.util.*;
import java.util.concurrent.*;

public class Controller {
    private final Map<String, Integer> map;
    private final Storages storages;
    private final Request request;

    private ExecutorService service;
    private Future<?> future;

    public Controller(Factory factory, Storages storages, Map<String, Integer> map) {
        request = new Request(factory, storages);
        this.storages = storages;
        this.map = map;
    }

    public void start() {
        service = Executors.newFixedThreadPool(1);
        future = service.submit(request);
    }

    public void signal() {
        if (storages.carStorage().size() != map.get(Config.AUTO_SIZE.name())) {
            request.setSignal(true);
        }
    }

    public void finish() {
        service.shutdown();
        request.setFlag(false);

        try {
            if (!service.awaitTermination(3, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean check() {
        return future.isDone();
    }
}
