package ru.nsu.gunko.factory.commands.arithmetic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.gunko.Context;

class SqrtTest {

    @Test
    void perform() {
        Context context = new Context();
        Sqrt sqrt = new Sqrt();

        context.getStack().push(9.0);
        sqrt.perform(context);

        Assertions.assertEquals(3, context.getStack().peek());
    }
}