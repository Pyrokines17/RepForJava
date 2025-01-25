package ru.nsu.gunko.model.car;

import ru.nsu.gunko.model.parts.body.*;
import ru.nsu.gunko.model.parts.motor.*;
import ru.nsu.gunko.model.parts.accessory.*;

import java.util.UUID;

public record Car(Body body, Motor motor, Accessory accessory, UUID id) { }
