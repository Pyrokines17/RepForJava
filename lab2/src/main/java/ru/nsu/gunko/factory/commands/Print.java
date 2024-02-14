package ru.nsu.gunko.factory.commands;

import ru.nsu.gunko.Context;

public class Print implements Operation {
    @Override
    public void perform(Context context) {
        if (!context.getStack().isEmpty()) {
            System.out.println(context.getStack().peek());
        } // empty stack
    }
}
