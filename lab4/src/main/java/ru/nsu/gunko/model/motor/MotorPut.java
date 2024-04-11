package ru.nsu.gunko.model.motor;

import java.util.concurrent.BlockingQueue;

public class MotorPut implements Runnable {
    private final BlockingQueue<Motor> queue;
    private int count;
    private int time;

    public MotorPut(BlockingQueue<Motor> queue) {
        this.queue = queue;
        count = 0;
        time = 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                queue.put(new Motor(count));

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
