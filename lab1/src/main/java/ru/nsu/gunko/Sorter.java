package ru.nsu.gunko;

import java.util.*;

public class Sorter {
    public static void sortByValue(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        map.clear();

        for (Map.Entry<String, Integer> entry : list) {
            map.put(entry.getKey(), entry.getValue());
        }
    }
}
