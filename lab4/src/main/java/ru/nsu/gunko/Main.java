package ru.nsu.gunko;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.oth.*;
import ru.nsu.gunko.model.factory.*;
import ru.nsu.gunko.model.oth.Suppliers.*;
import ru.nsu.gunko.model.oth.controller.*;

import java.util.Map;

public class Main { //ToDo: a graphic
    public static void main(String[] args) {
        Preparer preparer = new Preparer();

        Map<String, Integer> map = preparer.readLine(args[0]);
        Storages storages = preparer.createStorages(map);

        Suppliers suppliers = new Suppliers(map);
        suppliers.start(storages);

        Factory factory = new Factory(storages);
        factory.start(map);

        Controller controller = new Controller(factory, storages, map);
        controller.start();

        Dealers dealers = new Dealers(map, storages, controller);
        dealers.start();

        synchronized (Thread.currentThread()) {
            try {
                Thread.currentThread().wait(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        preparer.end(suppliers, factory, dealers, controller);

        while (true) {
            if (preparer.checkDone(suppliers, factory, dealers, controller)) {
                break;
            } else {
                synchronized (Thread.currentThread()) {
                    try {
                        Thread.currentThread().wait(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}