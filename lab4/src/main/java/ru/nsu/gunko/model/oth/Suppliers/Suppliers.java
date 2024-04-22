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

    private Future<?> futureOfBody;
    private Future<?> futureOfMotor;
    private Services services;
    private Puts puts;

    public Suppliers(Map<String, Integer> map, Model model) {
        this.model = model;
        this.map = map;
        this.list = new ArrayList<>();
    }

    public void start(Storages storages) {
        ExecutorService serOfBodyAndMotor = Executors.newFixedThreadPool(2);
        //ExecutorService serOfBodyAndMotor = new CustomPool(2, new LinkedBlockingQueue<>());

        BodyPut bodyPut = new BodyPut(storages.bodyStorage(), model);
        bodyPut.setTime(50);
        futureOfBody = serOfBodyAndMotor.submit(bodyPut);

        MotorPut motorPut = new MotorPut(storages.motorStorage(), model);
        motorPut.setTime(50);
        futureOfMotor = serOfBodyAndMotor.submit(motorPut);

        int countSuppliers = map.get(Config.SUPPLIERS.name());
        ExecutorService serOfSuppliers = Executors.newFixedThreadPool(countSuppliers);
        //ExecutorService serOfSuppliers = new CustomPool(countSuppliers, new LinkedBlockingQueue<>());

        AccessoryPut accessoryPut = new AccessoryPut(storages.accessoryStorage(), model);
        accessoryPut.setTime(50);

        for (int i = 0; i < countSuppliers; ++i) {
            list.add(serOfSuppliers.submit(accessoryPut));
        }

        services = new Services(serOfBodyAndMotor, serOfSuppliers);
        puts = new Puts(bodyPut, motorPut, accessoryPut);
    }

    public void finish() {
        services.serviceOfBodyAndMotor().shutdown();
        puts.accessoryPut().setFlag(false);
        puts.motorPut().setFlag(false);

        services.serviceOfAccessory().shutdown();
        puts.bodyPut().setFlag(false);

        try {
            if (!services.serviceOfBodyAndMotor().awaitTermination(3, TimeUnit.SECONDS)) {
                services.serviceOfBodyAndMotor().shutdownNow();
            }

            if (!services.serviceOfAccessory().awaitTermination(3, TimeUnit.SECONDS)) {
                services.serviceOfAccessory().shutdownNow();
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

        return futureOfBody.isDone() && futureOfMotor.isDone();
    }

    public Puts getPuts() {
        return puts;
    }
}
