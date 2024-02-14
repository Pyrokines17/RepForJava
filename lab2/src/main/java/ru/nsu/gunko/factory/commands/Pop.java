package ru.nsu.gunko.factory.commands;

import ru.nsu.gunko.Context;

public class Pop implements Operation {
    @Override
    public void perform(Context context) {
        context.getStack().pop();
    }
}
