package ru.nsu.gunko.model.oth.Suppliers;

import java.util.concurrent.*;

public record Services(ExecutorService serviceOfBodyAndMotor, ExecutorService serviceOfAccessory) { }
