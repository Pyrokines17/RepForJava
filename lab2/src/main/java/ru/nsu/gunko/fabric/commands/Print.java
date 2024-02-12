package ru.nsu.gunko.fabric.commands;

import ru.nsu.gunko.Context;

public class Print implements Operation {
    @Override
    public void perform(Context context) {
        System.out.println(context.stack.peek());
    }
}
