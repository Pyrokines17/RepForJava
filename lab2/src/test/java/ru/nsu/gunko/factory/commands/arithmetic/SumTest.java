package ru.nsu.gunko.factory.commands.arithmetic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.gunko.Context;

class SumTest {

    @Test
    void perform() {
        Context context = new Context();
        Sum sum = new Sum();

        context.getStack().push(12.0);
        context.getStack().push(23.0);
        sum.perform(context);

        Assertions.assertEquals(context.getStack().peek(), 35);
    }
}