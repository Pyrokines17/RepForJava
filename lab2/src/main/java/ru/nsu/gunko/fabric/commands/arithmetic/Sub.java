package ru.nsu.gunko.fabric.commands.arithmetic;

import ru.nsu.gunko.Context;
import ru.nsu.gunko.fabric.commands.Operation;

public class Sub implements Operation {
    @Override
    public void perform(Context context) {
        double second = context.stack.pop(),
                result = context.stack.pop() - second;
        context.stack.push(result);
    }
}
