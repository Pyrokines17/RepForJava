package ru.nsu.gunko.model.oth.controller;

import ru.nsu.gunko.model.base.Model;
import ru.nsu.gunko.model.factory.*;

public class Request implements Runnable {
    private final Model model;
    private final Factory factory;
    private boolean signal;
    private boolean flag;
    private int time;

    public Request(Factory factory, Model model) {
        this.model = model;
        this.factory = factory;
        this.signal = false;
        this.flag = true;
    }

    @Override
    public void run() {
        while (flag || model.getStorages().check() || !model.getStorages().carStorage().isEmpty()) {
            if (signal) {
                synchronized (factory) {
                    factory.signal();
                }
            }

            synchronized (Thread.currentThread()) {
                try {
                    Thread.currentThread().wait(time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public synchronized void setSignal(boolean signal) {
        this.signal = signal;
    }
}
