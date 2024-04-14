package ru.nsu.gunko;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.factory.Factory;
import ru.nsu.gunko.model.oth.*;
import ru.nsu.gunko.model.oth.controller.Controller;

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

        //preparer.end(suppliers, factory, dealers, controller);
    }
}