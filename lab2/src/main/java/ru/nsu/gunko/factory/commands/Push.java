package ru.nsu.gunko.factory.commands;

import ru.nsu.gunko.Context;
import ru.nsu.gunko.factory.exceptions.*;

public class Push implements Operation {
    @Override
    public void perform(Context context) {
        if (context.getArguments().isEmpty()) {
            throw new NotEnoughParamException("in Push not enough param");
        }

        String str = context.getArguments().get(0);

        if (str.matches("-?\\d+(\\.\\d+)?")) {
            context.getStack().push(Double.valueOf(str));
        } else {
            if (context.getMap().containsKey(str)) {
                context.getStack().push(context.getMap().get(str));
            } else {
                throw new DefineNotFoundException("in Push unknown param");
            }
        }
    }
}
