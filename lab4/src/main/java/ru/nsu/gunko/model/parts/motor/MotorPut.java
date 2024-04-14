package ru.nsu.gunko.model.parts.motor;

import java.util.concurrent.BlockingQueue;

public class MotorPut implements Runnable {
    private final BlockingQueue<Motor> queue;
    private boolean flag;
    private int count;
    private int time;

    public MotorPut(BlockingQueue<Motor> queue) {
        this.queue = queue;
        flag = true;
        count = 0;
        time = 0;
    }

    @Override
    public void run() {
        while (flag) {
            try {
                synchronized (this) {
                    this.queue.put(new Motor(count));
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

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
