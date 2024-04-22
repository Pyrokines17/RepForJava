package ru.nsu.gunko.model.parts.body;

import ru.nsu.gunko.model.Storages;
import ru.nsu.gunko.model.base.*;

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
        Storages storages = model.getStorages();
        while (flag || queue.size() < storages.motorStorage().size() || queue.size() < storages.accessoryStorage().size()) {
            try {
                synchronized (this) {
                    synchronized (queue) {
                        queue.put(new Body(count));
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
