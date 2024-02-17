package ru.nsu.gunko;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

class CommandReaderTest {

    @Test
    void getCommands() {
        List<List<String>> result = new ArrayList<>(),
                commands = new ArrayList<>();
        List<String> command1 = new ArrayList<>(),
                command2 = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("test1"))) {
            CommandReader cr = new CommandReader(br);
            result = cr.getCommands();
        } catch (IOException e) {
            System.err.println("Error while reading commands: " + e.getLocalizedMessage());
        }

        command1.add("+");
        commands.add(command1);

        command2.add("PUSH");
        command2.add("123");
        commands.add(command2);

        Assertions.assertEquals(result, commands);
    }
}