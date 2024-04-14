package ru.nsu.gunko.model;

import ru.nsu.gunko.model.parts.accessory.Accessory;
import ru.nsu.gunko.model.car.Car;
import ru.nsu.gunko.model.parts.motor.Motor;
import ru.nsu.gunko.model.parts.body.Body;

import java.util.concurrent.BlockingQueue;

public record Storages(BlockingQueue<Body> bodyStorage, BlockingQueue<Motor> motorStorage,
                       BlockingQueue<Accessory> accessoryStorage, BlockingQueue<Car> carStorage) { }
