package ru.nsu.gunko.fabric.commands;

import ru.nsu.gunko.Context;

public class Print implements Operation {
    @Override
    public void perform(Context context) {
        if (!context.stack.isEmpty()) {
            System.out.println(context.stack.peek());
        } // empty stack
    }
}
