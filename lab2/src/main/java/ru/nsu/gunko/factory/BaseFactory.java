package ru.nsu.gunko.factory;

import ru.nsu.gunko.factory.commands.Operation;
import ru.nsu.gunko.factory.exceptions.*;

import java.io.*;
import java.util.*;

public class BaseFactory implements Factory {
    private final Map<String, String> arrOfClass;

    public BaseFactory() throws IOException {
        arrOfClass = this.getMap();
    }

    @Override
    public Operation getOperation(List<String> command) {
        String nameOfClass;

        if (arrOfClass.containsKey(command.get(0))) {
            nameOfClass = arrOfClass.get(command.get(0));
        } else {
            throw new NameOfClassNotFoundException(command.get(0));
        }

        Operation operation;

        try {
            operation = (Operation) Class.forName(nameOfClass).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new BadCreatingException(command.get(0), e);
        }

        return operation;
    }

    private Map<String, String> getMap() throws IOException {
        Map<String, String> map = new HashMap<>();
        String line;

        try (InputStream is = this.getClass().getResourceAsStream("/Conf");
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)
        ) {
            while ((line = br.readLine()) != null) {
                String[] sepStr = line.split("\\s+");
                map.put(sepStr[0], sepStr[1]);
            }
        }

        return map;
    }
}
