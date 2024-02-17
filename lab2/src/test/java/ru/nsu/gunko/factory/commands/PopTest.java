package ru.nsu.gunko.factory.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.gunko.Context;

class PopTest {

    @Test
    void perform() {
        Context context = new Context();
        Pop pop = new Pop();

        context.getStack().push(12.0);
        context.getStack().push(23.0);
        pop.perform(context);

        Assertions.assertEquals(context.getStack().peek(), 12.0);
    }
}