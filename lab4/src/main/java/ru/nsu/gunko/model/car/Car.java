package ru.nsu.gunko.model.car;

import ru.nsu.gunko.model.parts.body.Body;
import ru.nsu.gunko.model.parts.motor.Motor;
import ru.nsu.gunko.model.parts.accessory.Accessory;

public record Car(Body body, Motor motor, Accessory accessory, int id) { }
