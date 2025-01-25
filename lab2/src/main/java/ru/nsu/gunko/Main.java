package ru.nsu.gunko;

import ru.nsu.gunko.factory.commands.Operation;
import ru.nsu.gunko.factory.exceptions.*;
import ru.nsu.gunko.factory.*;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<List<String>> commands = new ArrayList<>();
        Context context = new Context();
        ArgsParser argsParser = new ArgsParser();

        List<String> list = argsParser.parse(Arrays.asList(args));

        if (!list.get(0).equals("none")) {
            try (BufferedReader br = new BufferedReader(new FileReader(list.get(0)))) {
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
            Factory factory;

            if (list.get(1).equals("base")) {
                factory = new BaseFactory();
            } else {
                Factory base = new BaseFactory();
                factory = new AddFactory(base);
            }

            for (List<String> command : commands) {
                try {
                    Operation operation = factory.getOperation(command);

                    if (command.size() > 1) {
                        context.getArguments().addAll(command.subList(1, command.size()));
                    }

                    operation.perform(context);
                } catch (EmptyStackException e) {
                    System.err.println("Can't do perform (empty stack): " + command.get(0));
                } catch (DefineNotFoundException | NotEnoughParamException e) {
                    System.err.println("Error while do perform: " + e.getLocalizedMessage());
                } catch (NameOfClassNotFoundException e) {
                    System.err.println("Error while finding operation: " + e.getLocalizedMessage());
                } catch (BadCreatingException e) {
                    System.err.println("Error while creating operation: " + e.getLocalizedMessage());
                }

                if (!context.getArguments().isEmpty()) {
                    context.getArguments().clear();
                }
            }
        } catch (IOException e) {
            System.err.println("Error while creating factory: " + e.getLocalizedMessage());
        }
    }
}