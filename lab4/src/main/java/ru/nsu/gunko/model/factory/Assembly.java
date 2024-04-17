package ru.nsu.gunko.model.factory;

import ru.nsu.gunko.model.*;
import ru.nsu.gunko.model.car.*;
import ru.nsu.gunko.model.base.*;
import ru.nsu.gunko.model.parts.body.*;
import ru.nsu.gunko.model.parts.motor.*;
import ru.nsu.gunko.model.parts.accessory.*;

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
        while (flag || storages.check()) {
            if (signal) {
                synchronized (this) {
                    try {
                        synchronized (storages) {
                            Body body = storages.bodyStorage().take();
                            Motor motor = storages.motorStorage().take();
                            Accessory accessory = storages.accessoryStorage().take();

                            Car car = new Car(body, motor, accessory, count);
                            storages.carStorage().put(car);
                            size = storages.carStorage().size();
                        }

                        this.signal = false;
                        ++this.count;

                        synchronized (model) {
                            int ID = 4;
                            model.setState(State.CHANGE_STAT);
                            model.notifyUnsafe(ID);
                        }
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

    public int getCount() {
        return count;
    }

    public int getSize() {
        return size;
    }
}
