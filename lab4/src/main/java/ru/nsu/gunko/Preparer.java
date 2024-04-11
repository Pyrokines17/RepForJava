package ru.nsu.gunko;

import java.io.*;
import java.util.*;

public class Preparer {
    public static Map<String, Integer> readLine(String name) {
        Map<String, Integer> map = new HashMap<>();
        String[] parts;
        String line;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(name))) {
            while ((line = fileReader.readLine()) != null) {
                parts = line.split("=");
                map.put(parts[0], Integer.parseInt(parts[1]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return map;
    }
}
