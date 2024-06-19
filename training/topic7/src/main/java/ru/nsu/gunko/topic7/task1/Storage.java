package ru.nsu.gunko.topic7.task1;

import java.util.*;

public class Storage<T> {
    List<T> box;
    int count;

    Storage(int count) {
        this.count = count;
        box = new ArrayList<>();
    }

    synchronized public void put(T obj) throws InterruptedException {
        while (box.size() == count) {
            wait();
        }

        box.add(obj);
        notifyAll();
    }

    synchronized public T take() throws InterruptedException {
        while (box.isEmpty()) {
            wait();
        }

        T obj = box.getLast();
        box.removeLast();
        notifyAll();

        return obj;
    }
}
