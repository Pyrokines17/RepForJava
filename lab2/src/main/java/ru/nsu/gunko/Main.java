package ru.nsu.gunko;

import ru.nsu.gunko.fabric.*;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String[]> commands = new ArrayList<>();
        Context context = new Context();

        if (args.length == 1) {
            try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
                CommandReader cr = new CommandReader(br);
                commands = cr.getCommands();
            } catch (IOException e) {
                System.out.println("Error while reading commands: " + e.getLocalizedMessage());
            }
        } else {
            Scanner in = new Scanner(System.in);
            String line = in.nextLine();

            while (!line.isEmpty()) {
                String[] sepStr = line.split("\\s+");
                commands.add(sepStr);
                line = in.nextLine();
            }
        }

        try {
            Fabric fabric = new Fabric(context);
            fabric.process(commands);
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println("Error while fabric work: " + e.getLocalizedMessage());
        }
    }
}