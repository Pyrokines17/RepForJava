package ru.nsu.gunko;

import java.io.*;
import java.util.*;

public class CommandReader {
    private final BufferedReader bufReader;

    public CommandReader(BufferedReader newReader) {
        bufReader = newReader;
    }

    public List<List<String>> getCommands() throws IOException {
        String line;
        List<List<String>> res = new ArrayList<>();

        while ((line = bufReader.readLine()) != null) {
            String[] sepStr = line.split("\\s+");
            res.add(new ArrayList<>(Arrays.asList(sepStr)));
        }

        return res;
    }
}
