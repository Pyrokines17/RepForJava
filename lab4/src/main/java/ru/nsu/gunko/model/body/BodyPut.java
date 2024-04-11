package ru.nsu.gunko.model.body;

import java.util.concurrent.*;

public class BodyPut implements Runnable {
    private final BlockingQueue<Body> queue;
    private int count;
    private int time;

    public BodyPut(BlockingQueue<Body> queue) {
        this.queue = queue;
        count = 0;
        time = 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                queue.put(new Body(count));

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
