package ru.nsu.gunko.model.oth;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.parts.accessory.AccessoryPut;
import ru.nsu.gunko.model.parts.body.BodyPut;
import ru.nsu.gunko.model.parts.motor.MotorPut;

import java.util.Map;
import java.util.concurrent.*;

public class Suppliers {
    private final Map<String, Integer> map;
    private BodyPut bodyPut;
    private MotorPut motorPut;
    private AccessoryPut accessoryPut;

    public Suppliers(Map<String, Integer> map) {
        this.map = map;
    }

    public void start(Storages storages) {
        ExecutorService serOfBodyAndMotor = Executors.newFixedThreadPool(2);

        bodyPut = new BodyPut(storages.bodyStorage());
        bodyPut.setTime(75);
        serOfBodyAndMotor.submit(bodyPut);

        motorPut = new MotorPut(storages.motorStorage());
        motorPut.setTime(75);
        serOfBodyAndMotor.submit(motorPut);

        int countSuppliers = map.get(Config.SUPPLIERS.name());
        ExecutorService serOfSuppliers = Executors.newFixedThreadPool(countSuppliers);

        accessoryPut = new AccessoryPut(storages.accessoryStorage());
        accessoryPut.setTime(75);

        for (int i = 0; i < countSuppliers; ++i) {
            serOfSuppliers.submit(accessoryPut);
        }
    }

    public void finish() {
        accessoryPut.setFlag(false);
        motorPut.setFlag(false);
        bodyPut.setFlag(false);
    }
}
