package ru.nsu.gunko.model;

import ru.nsu.gunko.model.car.*;
import ru.nsu.gunko.model.parts.body.*;
import ru.nsu.gunko.model.parts.motor.*;
import ru.nsu.gunko.model.parts.accessory.*;

import java.util.concurrent.BlockingQueue;

public record Storages(BlockingQueue<Body> bodyStorage, BlockingQueue<Motor> motorStorage,
                       BlockingQueue<Accessory> accessoryStorage, BlockingQueue<Car> carStorage) {
    public boolean check() {
        return !bodyStorage.isEmpty() && !motorStorage.isEmpty() && !accessoryStorage.isEmpty();
    }
}
