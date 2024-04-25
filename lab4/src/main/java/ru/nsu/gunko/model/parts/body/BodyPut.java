package ru.nsu.gunko.model.parts.body;

import ru.nsu.gunko.model.base.*;

import java.util.UUID;
import java.util.concurrent.*;

public class BodyPut implements Runnable {
    private final BlockingQueue<Body> queue;
    private final Model model;

    private boolean flag;
    private int count;
    private double time;

    public BodyPut(BlockingQueue<Body> queue, Model model) {
        this.queue = queue;
        this.model = model;

        flag = true;
        count = 0;
        time = 0;
    }

    @Override
    public void run() {
        while (flag) {
            try {
                synchronized (this) {
                    synchronized (queue) {
                        queue.put(new Body(UUID.randomUUID()));
                    }

                    ++this.count;

                    synchronized (model) {
                        model.setState(State.CHANGE_STAT);
                        model.notifyUnsafe();
                    }
                }

                synchronized (Thread.currentThread()) {
                    Thread.currentThread().wait((long) time);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        model.notifyAll();
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getCount() {
        return count;
    }
}
