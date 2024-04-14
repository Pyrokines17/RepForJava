package ru.nsu.gunko.model.oth.Suppliers;

import java.util.concurrent.ExecutorService;

public record Services(ExecutorService serviceOfBodyAndMotor, ExecutorService serviceOfAccessory) { }
