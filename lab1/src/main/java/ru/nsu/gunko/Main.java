package ru.nsu.gunko;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> quantity = new HashMap<>();
        int countOfWords = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            TextAnalyzer analyzer = new TextAnalyzer(reader);

            while (!analyzer.checkEnd()) {
                StringBuilder word = analyzer.getWord();
                String keyWord = word.toString();

                if (keyWord.isEmpty()) {
                    continue;
                }

                if (quantity.containsKey(keyWord)) {
                    quantity.replace(keyWord, quantity.get(keyWord) + 1);
                } else {
                    quantity.put(keyWord, 1);
                }

                ++countOfWords;
            }
        } catch (IOException e) {
            System.err.println("Error while reading file: " + e.getLocalizedMessage());
        }

        List<Map.Entry<String, Integer>> list = Sorter.sortByValue(quantity);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("result.csv"))) {
            for (Map.Entry<String, Integer> word : list) {
                double percent = (double)word.getValue() / countOfWords * 100;
                String result = word.getKey() + ";" + word.getValue() + ";" + percent + "\n";
                writer.write(result);
            }
        } catch (IOException e) {
            System.err.println("Error while writing file: " + e.getLocalizedMessage());
        }
    }
}