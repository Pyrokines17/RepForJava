package ru.nsu.gunko.model.oth;

import ru.nsu.gunko.model.Config;
import ru.nsu.gunko.model.Storages;
import ru.nsu.gunko.model.car.Sell;
import ru.nsu.gunko.model.oth.controller.Controller;

import java.util.Map;
import java.util.concurrent.*;

public class Dealers {
    private final Map<String, Integer> map;
    private final Storages storages;
    private final Controller controller;
    private Sell sell;

    public Dealers(Map<String, Integer> map, Storages storages, Controller controller) {
        this.map = map;
        this.storages = storages;
        this.controller = controller;
    }

    public void start() {
        int countOfDealers = map.get(Config.DEALERS.name());
        ExecutorService service = Executors.newFixedThreadPool(countOfDealers);

        sell = new Sell(storages, controller);
        sell.setTime(75);

        for (int i = 0; i < countOfDealers; ++i) {
            service.submit(sell);
        }
    }

    public void finish() {
        sell.setFlag(false);
    }
}
