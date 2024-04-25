package ru.nsu.gunko.model.oth.Suppliers;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.base.*;
import ru.nsu.gunko.model.parts.body.*;
import ru.nsu.gunko.model.parts.motor.*;
import ru.nsu.gunko.model.parts.accessory.*;
import ru.nsu.gunko.threads.CustomPool;

import java.util.*;
import java.util.concurrent.*;

public class Suppliers {
    private final Model model;
    private final Map<String, Integer> map;
    private final List<Future<?>> list;

    private ExecutorService serOfSuppliers;
    private Puts puts;

    public Suppliers(Map<String, Integer> map, Model model) {
        this.model = model;
        this.map = map;
        this.list = new ArrayList<>();
    }

    public void start(Storages storages) {
        int countSuppliers = map.get(Config.SUPPLIERS.name());
        serOfSuppliers = Executors.newFixedThreadPool(countSuppliers);
        //serOfSuppliers = new CustomPool(countSuppliers*3, new LinkedBlockingQueue<>());

        AccessoryPut accessoryPut = new AccessoryPut(storages.accessoryStorage(), model);
        MotorPut motorPut = new MotorPut(storages.motorStorage(), model);
        BodyPut bodyPut = new BodyPut(storages.bodyStorage(), model);

        accessoryPut.setTime(50);
        motorPut.setTime(50);
        bodyPut.setTime(50);

        for (int i = 0; i < countSuppliers-2; ++i) {
            list.add(serOfSuppliers.submit(accessoryPut));
        }

        list.add(serOfSuppliers.submit(motorPut));
        list.add(serOfSuppliers.submit(bodyPut));

        puts = new Puts(bodyPut, motorPut, accessoryPut);
    }

    public void finish() {
        serOfSuppliers.shutdown();
        puts.accessoryPut().setFlag(false);
        puts.motorPut().setFlag(false);
        puts.bodyPut().setFlag(false);

        try {
            if (!serOfSuppliers.awaitTermination(5, TimeUnit.SECONDS)) {
                serOfSuppliers.shutdownNow();
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

    public Puts getPuts() {
        return puts;
    }
}
