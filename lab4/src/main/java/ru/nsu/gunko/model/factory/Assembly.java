package ru.nsu.gunko.model.factory;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.car.*;
import ru.nsu.gunko.model.base.*;
import ru.nsu.gunko.model.parts.body.*;
import ru.nsu.gunko.model.parts.motor.*;
import ru.nsu.gunko.model.parts.accessory.*;

public class Assembly implements Runnable {
    private final int ID = 4;
    private final Storages storages;
    private final Model model;
    private boolean signal;
    private boolean flag;
    private int count;

    public Assembly(Storages storages, Model model) {
        this.storages = storages;
        this.model = model;
        signal = false;
        flag = true;
        count = 0;
    }

    @Override
    public void run() {
        while (flag || storages.check()) {
            if (signal) {
                synchronized (this) {
                    try {
                        Body body = this.storages.bodyStorage().take();
                        Motor motor = this.storages.motorStorage().take();
                        Accessory accessory = this.storages.accessoryStorage().take();

                        Car car = new Car(body, motor, accessory, count);
                        this.storages.carStorage().put(car);

                        this.signal = false;
                        ++this.count;

                        this.model.setCount(this.storages.carStorage().size());
                        this.model.setUsed(count);

                        this.model.setState(State.CHANGE_STAT);
                        this.model.notifyUnsafe(ID);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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

    public void setSignal(boolean signal) {
        this.signal = signal;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
