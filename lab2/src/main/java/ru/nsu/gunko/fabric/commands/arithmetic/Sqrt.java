package ru.nsu.gunko.fabric.commands.arithmetic;

import ru.nsu.gunko.Context;
import ru.nsu.gunko.fabric.commands.Operation;

public class Sqrt implements Operation {
    @Override
    public void perform(Context context) {
        double result = Math.sqrt(context.stack.pop());
        context.stack.push(result);
    }
}
