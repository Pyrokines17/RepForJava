package ru.nsu.gunko.topic7.task1;

public class Consumer implements Runnable {
    private final Storage<String> storage;

    Consumer(Storage<String> s) {
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
