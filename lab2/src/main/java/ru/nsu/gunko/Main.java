package ru.nsu.gunko;

import ru.nsu.gunko.factory.Factory;
import ru.nsu.gunko.factory.commands.Operation;

import ru.nsu.gunko.factory.exceptions.*;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<List<String>> commands = new ArrayList<>();
        Context context = new Context();

        if (args.length == 1) {
            try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
                CommandReader cr = new CommandReader(br);
                commands = cr.getCommands();
            } catch (IOException e) {
                System.err.println("Error while reading commands: " + e.getLocalizedMessage());
            }
        } else {
            Scanner in = new Scanner(System.in);
            String line = in.nextLine();

            while (!line.isEmpty()) {
                String[] sepStr = line.split("\\s+");
                commands.add(new ArrayList<>(Arrays.asList(sepStr)));
                line = in.nextLine();
            }
        }

        try {
            Factory factory = new Factory();

            for (List<String> command : commands) {
                try {
                    Operation operation = factory.getOperation(command);

                    if (command.size() > 1) {
                        context.getArguments().addAll(command.subList(1, command.size()));
                    }

                    operation.perform(context);

                    if (!context.getArguments().isEmpty()) {
                        context.getArguments().clear();
                    }
                } catch (DefineNotFoundException | NotEnoughParamException e) {
                    System.err.println("Error with operation: " + e.getLocalizedMessage());
                } catch (NameOfClassNotFoundException e) {
                    System.err.println("Error while finding operation: " + e.getLocalizedMessage());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    System.err.println("Error while creating operation: " + e.getLocalizedMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error while creating factory: " + e.getLocalizedMessage());
        }
    }
}