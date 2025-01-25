package ru.nsu.gunko.factory.commands.arithmetic;

import ru.nsu.gunko.Context;
import ru.nsu.gunko.factory.commands.Operation;

public class Sqrt implements Operation {
    @Override
    public void perform(Context context) {
        double result = Math.sqrt(context.getStack().pop());
        context.getStack().push(result);
    }
}
