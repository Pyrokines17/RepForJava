package ru.nsu.gunko;

import java.util.*;

public class Sorter {
    public static List<Map.Entry<String, Integer>> sortByValue(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return list;
    }
}
