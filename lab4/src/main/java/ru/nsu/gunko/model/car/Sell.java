package ru.nsu.gunko.model.car;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.base.*;

import java.util.logging.*;

public class Sell implements Runnable {
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final long beginTime;
    private final Model model;

    private String string;
    private boolean flag;
    private double time;

    public Sell(Model model) {
        beginTime = System.currentTimeMillis();
        this.model = model;
        flag = true;
        time = 0;
    }

    @Override
    public void run() {
        while (flag || model.getStorages().check() || !model.getStorages().carStorage().isEmpty()) {
            try {
                synchronized (model) {
                    if (!model.getStorages().carStorage().isEmpty()) {
                        String name = Thread.currentThread().getName();
                        int num = Integer.parseInt(name.substring(name.length() - 1));

                        Car car = model.getStorages().carStorage().take();
                        long timeR = System.currentTimeMillis() - this.beginTime;

                        string = timeR + "ms: Dealer " + num + ": Auto " + car.id() +
                                " (Body: " + car.body().id() + ", Motor: " + car.motor().id() + ", Accessory: " + car.accessory().id() + ") ";

                        if (model.getSettings().get(Config.LOGS.name()).equals(1)) {
                            model.setState(State.WRITE_SELL);
                            model.notifyUnsafe(0);
                            logger.log(Level.INFO, string);
                        }
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

    public String getString() {
        return string;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
