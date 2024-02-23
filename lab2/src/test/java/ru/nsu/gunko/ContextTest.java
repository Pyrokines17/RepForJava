package ru.nsu.gunko;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class ContextTest {

    @Test
    void getStack() {
        Context context = new Context();
        Stack<Double> stack = new Stack<>();

        context.getStack().push(123.0);
        stack.push(123.0);

        Assertions.assertEquals(context.getStack(), stack);
    }

    @Test
    void getMap() {
        Context context = new Context();
        Map<String, Double> map = new HashMap<>();

        context.getMap().put("abc", 123.0);
        map.put("abc", 123.0);

        Assertions.assertEquals(context.getMap(), map);
    }

    @Test
    void getArguments() {
        Context context = new Context();
        List<String> list = new ArrayList<>();

        context.getArguments().add("abc");
        list.add("abc");

        Assertions.assertEquals(list, context.getArguments());
    }
}