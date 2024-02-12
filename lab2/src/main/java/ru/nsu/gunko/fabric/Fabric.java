package ru.nsu.gunko.fabric;

import ru.nsu.gunko.Context;
import ru.nsu.gunko.fabric.commands.Operation;

import java.io.*;
import java.util.*;

public class Fabric {
    Map<String, String> arrOfClass;
    Context context;

    public Fabric(Context newContext) throws IOException {
        context = newContext;
        arrOfClass = this.getMap();
    }

    public void process(List<String[]> commands) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (String[] command : commands) {
            String nameOfClass = arrOfClass.get(command[0]);

            if (command.length > 1) {
                context.arguments.addAll(Arrays.asList(command).subList(1, command.length));
            }

            Operation operation = (Operation) Class.forName(nameOfClass).newInstance();
            operation.perform(context);
            context.arguments.clear();
        }
    }

    private Map<String, String> getMap() throws IOException {
        Map<String, String> map = new HashMap<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader("Conf"))) {
            while ((line = br.readLine()) != null) {
                String[] sepStr = line.split("\\s+");
                map.put(sepStr[0], sepStr[1]);
            }
        }

        return map;
    }
}
