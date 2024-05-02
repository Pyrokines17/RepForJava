package ru.nsu.gunko.model.oth.controller;

import ru.nsu.gunko.model.base.Model;
import ru.nsu.gunko.model.factory.*;

public class Request implements Runnable {
    private final Factory factory;
    private boolean signal;
    private boolean flag;
    private final Model model;

    public Request(Factory factory, Model model) {
        this.factory = factory;
        this.signal = false;
        this.flag = true;
        this.model = model;
    }

    @Override
    public void run() {
        while (flag) {
            if (signal) {
                synchronized (model.getFactory()) {
                    factory.signal();
                    factory.notify();
                }
            }

            synchronized (model.getController()) {
                try {
                    model.getController().wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        synchronized (model.getSync()) {
            model.getSync().notifyAll();
        }
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public synchronized void setSignal(boolean signal) {
        this.signal = signal;
    }
}
