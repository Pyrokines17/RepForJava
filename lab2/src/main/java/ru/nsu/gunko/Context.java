package ru.nsu.gunko;

import java.util.*;

public class Context {
    private final Stack<Double> stack;
    private final Map<String, Double> map;
    private final List<String> arguments;

    public Context() {
        stack = new Stack<>();
        map = new HashMap<>();
        arguments = new ArrayList<>();
    }

    public Stack<Double> getStack() {
        return stack;
    }

    public Map<String, Double> getMap() {
        return map;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
