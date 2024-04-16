package ru.nsu.gunko.model.factory;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.base.*;

import java.util.*;
import java.util.concurrent.*;

public class Factory {
    private ExecutorService service;
    private final Assembly assembly;
    private final List<Future<?>> list;

    public Factory(Storages storages, Model model) {
        assembly = new Assembly(storages, model);
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

        try {
            if (!service.awaitTermination(3, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
