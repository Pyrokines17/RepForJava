package ru.nsu.gunko.factory.commands;

import ru.nsu.gunko.Context;

public class Define implements Operation {
    @Override
    public void perform(Context context) {
        context.getMap().put(context.getArguments().get(0), Double.valueOf(context.getArguments().get(1)));
    }
}
