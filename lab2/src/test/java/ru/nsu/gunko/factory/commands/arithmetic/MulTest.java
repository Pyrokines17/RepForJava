package ru.nsu.gunko.factory.commands.arithmetic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.gunko.Context;

class MulTest {

    @Test
    void perform() {
        Context context = new Context();
        Mul mul = new Mul();

        context.getStack().push(12.0);
        context.getStack().push(3.0);
        mul.perform(context);

        Assertions.assertEquals(36, context.getStack().peek());
    }
}