package ru.nsu.gunko.factory;

import ru.nsu.gunko.factory.commands.Operation;

import java.util.*;

public class AddFactory implements Factory {
    private final Factory factory;
    private final Map<String, Operation> map;

    public AddFactory(Factory newFactory) {
        factory = newFactory;
        map = new HashMap<>();
    }

    @Override
    public Operation getOperation(List<String> command) {
        if (!map.containsKey(command.get(0))) {
            Operation operation = factory.getOperation(command);
            map.put(command.get(0), operation);
            return operation;
        } else {
            return map.get(command.get(0));
        }
    }
}
