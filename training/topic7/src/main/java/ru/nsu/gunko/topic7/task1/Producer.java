package ru.nsu.gunko.topic7.task1;

public class Producer implements Runnable {
    private final Storage<String> storage;
    private int count;

    Producer(Storage<String> s) {
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

