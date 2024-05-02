package ru.nsu.gunko.model.oth.controller;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.base.Model;
import ru.nsu.gunko.model.factory.*;
import ru.nsu.gunko.threads.CustomPool;

import java.util.*;
import java.util.concurrent.*;

public class Controller {
    private final Map<String, Integer> map;
    private final Storages storages;
    private final Request request;

    private ExecutorService service;

    public Controller(Factory factory, Storages storages, Map<String, Integer> map, Model model) {
        this.request = new Request(factory, model);
        this.storages = storages;
        this.map = map;
    }

    public void start() {
        //service = Executors.newFixedThreadPool(1);
        service = new CustomPool(1, new LinkedBlockingQueue<>());
        service.submit(request);
    }

    public synchronized void signal() {
        if (storages.carStorage().size() != map.get(Config.AUTO_SIZE.name())) {
            request.setSignal(true);
        }
    }

    public void finish() {
        request.setFlag(false);
        service.shutdownNow();

        try {
            service.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
