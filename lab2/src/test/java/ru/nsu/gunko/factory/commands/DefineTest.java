package ru.nsu.gunko.factory.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.gunko.Context;

import java.util.HashMap;
import java.util.Map;

class DefineTest {

    @Test
    void perform() {
        Context context = new Context();
        Define define = new Define();
        Map<String, Double> map = new HashMap<>();

        context.getArguments().add("a");
        context.getArguments().add("12");
        define.perform(context);

        map.put("a", 12.0);

        Assertions.assertEquals(context.getMap(), map);
    }
}