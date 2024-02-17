package ru.nsu.gunko.factory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.gunko.CommandReader;
import ru.nsu.gunko.Context;
import ru.nsu.gunko.factory.commands.*;
import ru.nsu.gunko.factory.commands.arithmetic.*;

import java.io.*;
import java.util.*;

class FactoryTest {

    @Test
    void getOperation() {
        Context context1 = new Context(),
                context2 = new Context();
        List<String> list = new ArrayList<>();

        context1.getStack().push(12.0);
        context2.getStack().push(12.0);
        context1.getStack().push(3.0);
        context2.getStack().push(3.0);
        list.add("/");

        try {
            Factory factory = new Factory();

            try {
                Operation operation = factory.getOperation(list);
                operation.perform(context1);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.err.println("Error while creating operation: " + e.getLocalizedMessage());
            }
        } catch (IOException e) {
            System.err.println("Error while creating factory: " + e.getLocalizedMessage());
        }

        Div div = new Div();
        div.perform(context2);

        Assertions.assertEquals(context1.getStack(), context2.getStack());
    }
}