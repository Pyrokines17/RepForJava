package ru.nsu.gunko.factory.commands.arithmetic;

import ru.nsu.gunko.Context;
import ru.nsu.gunko.factory.commands.Operation;

public class Sum implements Operation {
    @Override
    public void perform(Context context) {
        double second = context.getStack().pop(),
                result = context.getStack().pop() + second;
        context.getStack().push(result);
    }
}