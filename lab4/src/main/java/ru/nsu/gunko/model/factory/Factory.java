package ru.nsu.gunko.model.factory;

import ru.nsu.gunko.model.Config;
import ru.nsu.gunko.model.Storages;

import java.util.Map;
import java.util.concurrent.*;

public class Factory {
    private final Assembly assembly;

    public Factory(Storages storages) {
        assembly = new Assembly(storages);
    }

    public void start(Map<String, Integer> map) {
        int countOfThreads = map.get(Config.WORKERS.name());
        ExecutorService service = Executors.newFixedThreadPool(countOfThreads);

        for (int i = 0; i < countOfThreads; ++i) {
            service.submit(assembly);
        }
    }

    public void signal() {
        assembly.setSignal(true);
    }

    public void finish() {
        assembly.setFlag(false);
    }

}
