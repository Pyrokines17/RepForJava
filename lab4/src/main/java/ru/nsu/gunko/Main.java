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

        Window window = new Window();
        Preparer preparer = new Preparer();

        Map<String, Integer> map = preparer.readLine(args[0]);
        Storages storages = preparer.createStorages(map);

        Model model = new Model(map, storages);
        window.setModel(model);
        model.setModelListener(window);

        model.start(map, storages);
        model.getController().signal();

        while (window.getFlag()) {
            synchronized (Thread.currentThread()) {
                try {
                    Thread.currentThread().wait(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        preparer.end(model);

        while (!preparer.checkDone(model)) {
            synchronized (Thread.currentThread()) {
                try {
                    Thread.currentThread().wait(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        window.setVisible(false);
        window.dispose();
    }
}