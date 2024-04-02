package ru.nsu.gunko.topic8.task1;

import java.util.concurrent.BlockingDeque;

public class Consumer implements Runnable {
    private final BlockingDeque<String> storage;

    Consumer(BlockingDeque<String> s) {
        storage = s;
    }

    public void run() {
        try {
            while (true) {
                consume(storage.take());
                synchronized (Thread.currentThread()) {
                    try {
                        Thread.currentThread().wait(75);
                    } catch (InterruptedException ex) {
                        System.err.println(ex.getLocalizedMessage());
                    }
                }
            }
        } catch (InterruptedException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

    void consume(Object x) {
        String name = Thread.currentThread().getName();
        System.out.println("c" + name.substring(name.length()-1) + " consumes " + x);
    }
}
