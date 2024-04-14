package ru.nsu.gunko.model.parts.body;

import java.util.concurrent.*;

public class BodyPut implements Runnable {
    private final BlockingQueue<Body> queue;
    private boolean flag;
    private int count;
    private int time;

    public BodyPut(BlockingQueue<Body> queue) {
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
                    this.queue.put(new Body(count));
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
