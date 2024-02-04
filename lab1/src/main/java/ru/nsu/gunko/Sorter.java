package ru.nsu.gunko;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;

public class Sorter {
    public static ArrayList<HashMap.Entry<String, Integer>> sortByValue(HashMap<String, Integer> map) {
        ArrayList<HashMap.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(HashMap.Entry.comparingByValue(Comparator.reverseOrder()));
        return list;
    }
}
