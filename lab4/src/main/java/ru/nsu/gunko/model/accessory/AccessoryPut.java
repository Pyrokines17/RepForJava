package ru.nsu.gunko.model.accessory;

import java.util.concurrent.BlockingQueue;

public class AccessoryPut implements Runnable {
    private final BlockingQueue<Accessory> queue;
    private int count;
    private int time;

    public AccessoryPut(BlockingQueue<Accessory> queue) {
        this.queue = queue;
        count = 0;
        time = 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                queue.put(new Accessory(count));

                synchronized (this) {
                    ++this.count;
                }

                synchronized (Thread.currentThread()) {
                    Thread.currentThread().wait(time);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setTime(int time) {
        this.time = time;
    }
}
