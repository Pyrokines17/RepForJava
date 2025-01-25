package ru.nsu.gunko.model.factory;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.car.*;
import ru.nsu.gunko.model.base.*;

import ru.nsu.gunko.model.parts.body.*;
import ru.nsu.gunko.model.parts.motor.*;
import ru.nsu.gunko.model.parts.accessory.*;

import java.util.UUID;

public class Assembly implements Runnable {
    private final Storages storages;
    private final Model model;

    private boolean signal;
    private boolean flag;

    private int count;
    private int size;

    public Assembly(Storages storages, Model model) {
        this.storages = storages;
        this.model = model;
        signal = false;
        flag = true;
        count = 0;
        size = 0;
    }

    @Override
    public void run() {
        while (flag) {
            if (signal) {
                synchronized (this) {
                    try {
                        synchronized (storages) {
                            Body body = storages.bodyStorage().take();
                            Motor motor = storages.motorStorage().take();
                            Accessory accessory = storages.accessoryStorage().take();

                            Car car = new Car(body, motor, accessory, UUID.randomUUID());
                            storages.carStorage().put(car);
                        }

                        this.signal = false;
                        ++this.count;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                size = storages.carStorage().size();
                synchronized (model) {
                    model.setState(State.CHANGE_STAT);
                    model.notifyUnsafe();
                }
            }

            synchronized (model.getFactory()) {
                try {
                    model.getFactory().wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        synchronized (model.getSync()) {
            model.getSync().notifyAll();
        }
    }

    public synchronized void setSignal(boolean signal) {
        this.signal = signal;
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
