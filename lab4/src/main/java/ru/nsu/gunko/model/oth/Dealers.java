package ru.nsu.gunko.model.oth;

import ru.nsu.gunko.model.Config;
import ru.nsu.gunko.model.Storages;
import ru.nsu.gunko.model.car.Sell;
import ru.nsu.gunko.model.oth.controller.*;

import java.util.*;
import java.util.concurrent.*;

public class Dealers {
    private final Map<String, Integer> map;
    private final Storages storages;
    private final Controller controller;
    private ExecutorService service;
    private final List<Future<?>> list;
    private Sell sell;

    public Dealers(Map<String, Integer> map, Storages storages, Controller controller) {
        this.map = map;
        this.storages = storages;
        this.controller = controller;
        list = new ArrayList<>();
    }

    public void start() {
        int countOfDealers = map.get(Config.DEALERS.name());
        service = Executors.newFixedThreadPool(countOfDealers);

        sell = new Sell(storages, controller);
        sell.setTime(75);

        for (int i = 0; i < countOfDealers; ++i) {
            list.add(service.submit(sell));
        }
    }

    public void finish() {
        service.shutdown();
        sell.setFlag(false);
    }

    public boolean check() {
        for (Future<?> future : list) {
            if (!future.isDone()) {
                return false;
            }
        }

        return true;
    }
}
