package ru.nsu.gunko.factory.commands;

import ru.nsu.gunko.Context;

public class Push implements Operation {
    @Override
    public void perform(Context context) {
        String str = context.getArguments().get(0);

        if (str.matches("-?\\d+(\\.\\d+)?")) {
            context.getStack().push(Double.valueOf(str));
        } else {
            if (context.getMap().containsKey(str)) {
                context.getStack().push(context.getMap().get(str));
            } //parameter not found
        }
    }
}
