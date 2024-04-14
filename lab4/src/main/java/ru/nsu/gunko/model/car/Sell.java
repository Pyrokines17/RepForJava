package ru.nsu.gunko.model.car;

import ru.nsu.gunko.model.Storages;
import ru.nsu.gunko.model.oth.controller.*;

public class Sell implements Runnable {
    private final Storages storages;
    private final Controller controller;
    private final long beginTime;
    private boolean flag;
    private int time;

    public Sell(Storages storages, Controller controller) {
        this.storages = storages;
        this.controller = controller;
        beginTime = System.currentTimeMillis();
        flag = true;
        time = 0;
    }

    @Override
    public void run() {
        while (flag || storages.check() || !storages.carStorage().isEmpty()) {
            try {
                synchronized (this) {
                    if (!storages.carStorage().isEmpty()) {
                        String name = Thread.currentThread().getName();
                        int num = Integer.parseInt(name.substring(name.length() - 1));

                        Car car = this.storages.carStorage().take();
                        long timeR = System.currentTimeMillis() - this.beginTime;

                        System.out.println(
                                timeR + "ms: Dealer " + num + ": Auto " + car.id() + " (Body: " + car.body().id() + ", Motor: " + car.motor().id() + ", Accessory: " + car.accessory().id() + ")"
                        );
                    }

                    this.controller.signal();
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
