package ru.nsu.gunko;

import ru.nsu.gunko.view.*;
import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.base.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Need conf...");
            System.exit(1);
        }

        Preparer preparer = new Preparer();

        Map<String, Integer> map = preparer.readLine(args[0]);
        Storages storages = preparer.createStorages(map);

        Model model = new Model(map, storages);
        Window window = new Window(model);
        model.setModelListener(window);

        model.start(map, storages);
        model.getController().signal();

        while (window.getFlag()) {
            try {
                synchronized (model) {
                    model.wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        preparer.end(model);
        window.setVisible(false);
        window.dispose();
    }
}