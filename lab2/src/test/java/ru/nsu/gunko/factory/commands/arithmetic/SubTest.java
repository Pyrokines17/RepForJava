package ru.nsu.gunko.factory.commands.arithmetic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.gunko.Context;

class SubTest {

    @Test
    void perform() {
        Context context = new Context();
        Sub sub = new Sub();

        context.getStack().push(12.0);
        context.getStack().push(3.0);
        sub.perform(context);

        Assertions.assertEquals(9, context.getStack().peek());
    }
}