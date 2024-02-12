package ru.nsu.gunko;

import java.util.*;

public class Context {
    public Stack<Double> stack;
    public Map<String, Double> map;
    public List<String> arguments;

    public Context() {
        stack = new Stack<>();
        map = new HashMap<>();
        arguments = new ArrayList<>();
    }
}
