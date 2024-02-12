package ru.nsu.gunko.fabric.commands;

import ru.nsu.gunko.Context;

public class Pop implements Operation {
    @Override
    public void perform(Context context) {
        context.stack.pop();
    }
}
