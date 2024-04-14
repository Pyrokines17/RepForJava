package ru.nsu.gunko.model.factory;

import ru.nsu.gunko.model.Config;
import ru.nsu.gunko.model.Storages;

import java.util.*;
import java.util.concurrent.*;

public class Factory {
    private ExecutorService service;
    private final Assembly assembly;
    private final List<Future<?>> list;

    public Factory(Storages storages) {
        assembly = new Assembly(storages);
        list = new ArrayList<>();
    }

    public void start(Map<String, Integer> map) {
        int countOfThreads = map.get(Config.WORKERS.name());
        service = Executors.newFixedThreadPool(countOfThreads);

        for (int i = 0; i < countOfThreads; ++i) {
            list.add(service.submit(assembly));
        }
    }

    public void signal() {
        assembly.setSignal(true);
    }

    public void finish() {
        service.shutdown();
        assembly.setFlag(false);
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
