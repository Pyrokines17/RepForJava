package ru.nsu.gunko.model;

import ru.nsu.gunko.model.factory.Factory;
import ru.nsu.gunko.model.car.Car;
import ru.nsu.gunko.model.oth.controller.Controller;
import ru.nsu.gunko.model.parts.accessory.Accessory;
import ru.nsu.gunko.model.parts.body.Body;
import ru.nsu.gunko.model.parts.motor.Motor;
import ru.nsu.gunko.model.oth.Dealers;
import ru.nsu.gunko.model.oth.Suppliers;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Preparer {
    public Storages createStorages(Map<String, Integer> map) {
        BlockingQueue<Body> bodyStorage = new ArrayBlockingQueue<>(map.get(Config.BODY_SIZE.name()));
        BlockingQueue<Motor> motorStorage = new ArrayBlockingQueue<>(map.get(Config.MOTOR_SIZE.name()));
        BlockingQueue<Accessory> accessoryStorage = new ArrayBlockingQueue<>(map.get(Config.ACCESSORY_SIZE.name()));
        BlockingQueue<Car> carStorage = new ArrayBlockingQueue<>(map.get(Config.AUTO_SIZE.name()));
        return new Storages(bodyStorage, motorStorage, accessoryStorage, carStorage);
    }

    public Map<String, Integer> readLine(String name) {
        Map<String, Integer> map = new HashMap<>();
        String[] parts;
        String line;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(name))) {
            while ((line = fileReader.readLine()) != null) {
                parts = line.split("=");
                map.put(parts[0], Integer.parseInt(parts[1]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    public void end(Suppliers suppliers, Factory factory, Dealers dealers, Controller controller) {
        suppliers.finish();
        factory.finish();
        dealers.finish();
        controller.finish();
    }
}
