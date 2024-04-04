package ru.nsu.gunko.topic8.task1;

import java.util.concurrent.BlockingDeque;

public class Producer implements Runnable {
    private final BlockingDeque<String> storage;
    private int count;
    private final CommonRes commonRes;

    Producer(BlockingDeque<String> s, CommonRes resP) {
        storage = s;
        count = 1;
        commonRes = resP;
    }

    public void run() {
        try {
            while (commonRes.getFlag()) {
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

