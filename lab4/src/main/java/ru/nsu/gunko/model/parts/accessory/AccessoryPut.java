package ru.nsu.gunko.model.parts.accessory;

import ru.nsu.gunko.model.base.*;

import java.util.concurrent.*;

public class AccessoryPut implements Runnable {
    private final BlockingQueue<Accessory> queue;
    private final Model model;
    private boolean flag;
    private int count;
    private int size;
    private double time;

    public AccessoryPut(BlockingQueue<Accessory> queue, Model model) {
        this.queue = queue;
        this.model = model;
        flag = true;
        count = 0;
        size = 0;
        time = 0;
    }

    @Override
    public void run() {
        while (flag) {
            try {
                synchronized (this) {
                    synchronized (queue) {
                        queue.put(new Accessory(count));
                        size = queue.size();
                    }

                    ++this.count;

                    synchronized (model) {
                        int ID = 3;
                        model.setState(State.CHANGE_STAT);
                        model.notifyUnsafe(ID);
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

    public int getSize() {
        return size;
    }
}
