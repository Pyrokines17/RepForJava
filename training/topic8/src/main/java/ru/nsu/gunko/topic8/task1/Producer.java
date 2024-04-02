package ru.nsu.gunko.topic8.task1;

import java.util.concurrent.BlockingDeque;

public class Producer implements Runnable {
    private final BlockingDeque<String> storage;
    private int count;

    Producer(BlockingDeque<String> s) {
        storage = s;
        count = 1;
    }

    public void run() {
        try {
            while (true) {
                storage.put(produce());
                ++count;
            }
        } catch (InterruptedException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

    String produce() {
        String name = Thread.currentThread().getName();
        return "p" + name.substring(name.length()-1) + "-" + count;
    }
}

