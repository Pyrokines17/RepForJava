package ru.nsu.gunko.model.oth;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.car.*;
import ru.nsu.gunko.model.base.*;
import ru.nsu.gunko.threads.CustomPool;

import java.util.*;
import java.util.concurrent.*;

public class Dealers {
    private final Map<String, Integer> map;
    private final List<Future<?>> list;
    private ExecutorService service;
    private final Model model;
    private Sell sell;

    public Dealers(Map<String, Integer> map, Model model) {
        this.list = new ArrayList<>();
        this.model = model;
        this.map = map;
    }

    public void start() {
        int countOfDealers = map.get(Config.DEALERS.name());
        //service = Executors.newFixedThreadPool(countOfDealers);
        service = new CustomPool(countOfDealers, new LinkedBlockingQueue<>());

        sell = new Sell(model);
        sell.setTime(50);

        for (int i = 0; i < countOfDealers; ++i) {
            list.add(service.submit(sell));
        }
    }

    public void finish() {
        sell.setFlag(false);
        service.shutdownNow();

        try {
            service.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Sell getSell() {
        return sell;
    }
}
