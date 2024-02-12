package ru.nsu.gunko.fabric.commands;

import ru.nsu.gunko.Context;

public class Define implements Operation {
    @Override
    public void perform(Context context) {
        context.map.put(context.arguments.get(0), Double.valueOf(context.arguments.get(1)));
    }
}
