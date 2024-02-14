package ru.nsu.gunko.factory;

import ru.nsu.gunko.Context;
import ru.nsu.gunko.factory.commands.Operation;

import java.io.*;
import java.util.*;

public class Factory {
    private final Map<String, String> arrOfClass;
    private final Context context;

    public Factory(Context newContext) throws IOException {
        context = newContext;
        arrOfClass = this.getMap();
    }

    public void process(List<String[]> commands) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (String[] command : commands) {
            String nameOfClass;

            if (arrOfClass.containsKey(command[0])) {
                nameOfClass = arrOfClass.get(command[0]);
            } else {
                continue;
            } //command not found

            if (command.length > 1) {
                context.getArguments().addAll(Arrays.asList(command).subList(1, command.length));
            }

            Operation operation = (Operation) Class.forName(nameOfClass).newInstance();
            operation.perform(context);
            context.getArguments().clear();
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
