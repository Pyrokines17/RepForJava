package controller;

import java.io.*;
import java.util.*;

public class CliController implements Controller{
    private final BufferedReader reader;

    public CliController() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String getLine() throws IOException {
        return reader.readLine();
    }

    public List<String> getSettings() throws IOException {
        List<String> settings = new ArrayList<>();

        System.out.println("Enter hostname:");
        settings.add(reader.readLine());

        System.out.println("Enter port:");
        settings.add(reader.readLine());

        return settings;
    }

    public void setCommand(String line) {}
}
