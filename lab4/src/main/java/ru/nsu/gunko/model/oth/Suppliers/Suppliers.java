package ru.nsu.gunko.model.oth.Suppliers;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.parts.body.*;
import ru.nsu.gunko.model.parts.motor.*;
import ru.nsu.gunko.model.parts.accessory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Suppliers {
    private final Map<String, Integer> map;
    private final List<Future<?>> list;
    private Future<?> futureOfBody;
    private Future<?> futureOfMotor;
    private Services services;
    private Puts puts;

    public Suppliers(Map<String, Integer> map) {
        this.map = map;
        list = new ArrayList<>();
    }

    public void start(Storages storages) {
        ExecutorService serOfBodyAndMotor = Executors.newFixedThreadPool(2);

        BodyPut bodyPut = new BodyPut(storages.bodyStorage());
        bodyPut.setTime(75);
        futureOfBody = serOfBodyAndMotor.submit(bodyPut);

        MotorPut motorPut = new MotorPut(storages.motorStorage());
        motorPut.setTime(75);
        futureOfMotor = serOfBodyAndMotor.submit(motorPut);

        int countSuppliers = map.get(Config.SUPPLIERS.name());
        ExecutorService serOfSuppliers = Executors.newFixedThreadPool(countSuppliers);

        AccessoryPut accessoryPut = new AccessoryPut(storages.accessoryStorage());
        accessoryPut.setTime(75);

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
    }

    public boolean check() {
        for (Future<?> future : list) {
            if (!future.isDone()) {
                return false;
            }
        }

        return futureOfBody.isDone() && futureOfMotor.isDone();
    }
}
