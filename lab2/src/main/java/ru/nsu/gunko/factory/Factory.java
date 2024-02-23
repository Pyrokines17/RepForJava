package ru.nsu.gunko.factory;

import ru.nsu.gunko.factory.commands.Operation;

import java.util.List;

public interface Factory {
    Operation getOperation(List<String> command);
}
