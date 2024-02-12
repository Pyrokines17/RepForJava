package ru.nsu.gunko.fabric.commands;

import ru.nsu.gunko.Context;

public class Push implements Operation {
    @Override
    public void perform(Context context) {
        String str = context.arguments.get(0);

        if (str.matches("-?\\d+(\\.\\d+)?")) {
            context.stack.push(Double.valueOf(str));
        } else {
            context.stack.push(context.map.get(str));
        }
    }
}
