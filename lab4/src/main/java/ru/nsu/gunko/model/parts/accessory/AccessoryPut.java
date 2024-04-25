package ru.nsu.gunko.model.parts.accessory;

import ru.nsu.gunko.model.Storages;
import ru.nsu.gunko.model.base.*;

import java.util.UUID;
import java.util.concurrent.*;

public class AccessoryPut implements Runnable {
    private final BlockingQueue<Accessory> queue;
    private final Model model;
    private boolean flag;
    private int count;
    private double time;

    public AccessoryPut(BlockingQueue<Accessory> queue, Model model) {
        this.queue = queue;
        this.model = model;
        flag = true;
        count = 0;
        time = 0;
    }

    @Override
    public void run() {
        Storages storages = model.getStorages();
        while (flag || queue.size() < storages.bodyStorage().size() || queue.size() < storages.motorStorage().size()) {
            try {
                synchronized (this) {
                    synchronized (queue) {
                        queue.put(new Accessory(UUID.randomUUID()));
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
