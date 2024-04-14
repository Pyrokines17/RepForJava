package ru.nsu.gunko.model.oth.controller;

import ru.nsu.gunko.model.Storages;
import ru.nsu.gunko.model.factory.Factory;

public class Request implements Runnable {
    private final Factory factory;
    private final Storages storages;
    private boolean signal;
    private boolean flag;

    public Request(Factory factory, Storages storages) {
        this.factory = factory;
        this.storages = storages;
        signal = false;
        flag = true;
    }

    @Override
    public void run() {
        while (flag || storages.check() || !storages.carStorage().isEmpty()) {
            if (signal) {
                synchronized (this) {
                    this.factory.signal();
                }
            }

            synchronized (Thread.currentThread()) {
                try {
                    Thread.currentThread().wait(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setSignal(boolean signal) {
        this.signal = signal;
    }
}
