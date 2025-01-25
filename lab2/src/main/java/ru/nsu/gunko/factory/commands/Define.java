package ru.nsu.gunko.factory.commands;

import ru.nsu.gunko.Context;
import ru.nsu.gunko.factory.exceptions.NotEnoughParamException;

public class Define implements Operation {
    @Override
    public void perform(Context context) {
        if (context.getArguments().size() > 1) {
            context.getMap().put(context.getArguments().get(0), Double.valueOf(context.getArguments().get(1)));
        } else {
            throw new NotEnoughParamException("in Define not enough param's");
        }
    }
}
