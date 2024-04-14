package ru.nsu.gunko.model.factory;

import ru.nsu.gunko.model.Storages;
import ru.nsu.gunko.model.parts.accessory.Accessory;
import ru.nsu.gunko.model.parts.body.Body;
import ru.nsu.gunko.model.car.Car;
import ru.nsu.gunko.model.parts.motor.Motor;

public class Assembly implements Runnable {
    private final Storages storages;
    private boolean signal;
    private boolean flag;
    private int count;

    public Assembly(Storages storages) {
        this.storages = storages;
        signal = false;
        flag = true;
        count = 0;
    }

    @Override
    public void run() {
        while (flag) {
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
