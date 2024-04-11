package ru.nsu.gunko;

import ru.nsu.gunko.model.accessory.*;
import ru.nsu.gunko.model.body.*;
import ru.nsu.gunko.model.motor.*;

import java.util.Map;
import java.util.concurrent.*;

public class Main { //ToDo: a graphic
    public static void main(String[] args) {
        Map<String, Integer> map = Preparer.readLine(args[0]);

        int count = map.get(Config.BODY_SIZE.name());

        BlockingQueue<Body> bodyStorage = new ArrayBlockingQueue<>(count);
        BlockingQueue<Motor> motorStorage = new ArrayBlockingQueue<>(map.get(Config.MOTOR_SIZE.name()));

        ExecutorService serOfBodyAndMotor = Executors.newFixedThreadPool(2);

        BodyPut bodyPut = new BodyPut(bodyStorage);
        bodyPut.setTime(75);
        serOfBodyAndMotor.submit(bodyPut);

        MotorPut motorPut = new MotorPut(motorStorage);
        motorPut.setTime(75);
        serOfBodyAndMotor.submit(motorPut);

        int countSuppliers = map.get(Config.SUPPLIERS.name());
        ExecutorService serOfSuppliers = Executors.newFixedThreadPool(countSuppliers);

        BlockingQueue<Accessory> accessoryStorage = new ArrayBlockingQueue<>(map.get(Config.ACCESSORY_SIZE.name()));
        AccessoryPut accessoryPut = new AccessoryPut(accessoryStorage);
        accessoryPut.setTime(75);

        for (int i = 0; i < countSuppliers; ++i) {
            serOfSuppliers.submit(accessoryPut);
        }


    }
}