package ru.nsu.gunko.model.oth.Suppliers;

import ru.nsu.gunko.model.parts.body.*;
import ru.nsu.gunko.model.parts.motor.*;
import ru.nsu.gunko.model.parts.accessory.*;

public record Puts(BodyPut bodyPut, MotorPut motorPut, AccessoryPut accessoryPut) { }
