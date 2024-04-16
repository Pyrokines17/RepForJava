package ru.nsu.gunko.model.parts.accessory;

import ru.nsu.gunko.model.base.*;

import java.util.concurrent.*;

public class AccessoryPut implements Runnable {
    private final int ID = 3;
    private final BlockingQueue<Accessory> queue;
    private final Model model;
    private boolean flag;
    private int count;
    private int time;

    public AccessoryPut(BlockingQueue<Accessory> queue, Model model) {
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
                    this.queue.put(new Accessory(count));
                    ++this.count;

                    this.model.setCount(queue.size());
                    this.model.setUsed(count);

                    this.model.setState(State.CHANGE_STAT);
                    this.model.notifyUnsafe(ID);
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
