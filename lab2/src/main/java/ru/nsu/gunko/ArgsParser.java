package ru.nsu.gunko;

import java.util.*;

public class ArgsParser {
    List<String> result;

    public ArgsParser() {
        result = new ArrayList<>();
    }

    public List<String> parse(List<String> args) {
        if (args.isEmpty()) {
            result.add("none");
            result.add("base");
        } else if (args.size() == 1) {
            if (args.get(0).equals("base") || args.get(0).equals("add")) {
                result.add("none");
                result.add(args.get(0));
            } else {
                result.add(args.get(0));
                result.add("base");
            }
        } else if (args.size() == 2) {
            if (args.get(0).equals("base") || args.get(0).equals("add")) {
                result.add(args.get(1));
                result.add(args.get(0));
            } else {
                result.add(args.get(0));
                result.add(args.get(1));
            }
        }
        return result;
    }
}
