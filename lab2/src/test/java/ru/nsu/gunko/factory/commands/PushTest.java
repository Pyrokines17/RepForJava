package ru.nsu.gunko.factory.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.gunko.Context;

import java.util.Stack;

class PushTest {

    @Test
    void perform() {
        Context context = new Context();
        Push push = new Push();
        Stack<Double> stack = new Stack<>();

        context.getArguments().add("12");
        push.perform(context);

        context.getArguments().clear();

        context.getMap().put("a", 23.0);
        context.getArguments().add("a");
        push.perform(context);

        stack.push(12.0);
        stack.push(23.0);

        Assertions.assertEquals(context.getStack(), stack);
    }
}