package ru.nsu.gunko.factory;

import ru.nsu.gunko.factory.commands.Operation;
import ru.nsu.gunko.factory.exceptions.NameOfClassNotFoundException;

import java.io.*;
import java.util.*;

public class Factory {
    private final Map<String, String> arrOfClass;

    public Factory() throws IOException {
        arrOfClass = this.getMap();
    }

    public Operation getOperation(List<String> command) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String nameOfClass;

        if (arrOfClass.containsKey(command.get(0))) {
            nameOfClass = arrOfClass.get(command.get(0));
        } else {
            throw new NameOfClassNotFoundException(command.get(0));
        }

        return (Operation) Class.forName(nameOfClass).newInstance();
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
