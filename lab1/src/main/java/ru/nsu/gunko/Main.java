package ru.nsu.gunko;

import java.io.*;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> quantity = new HashMap<>();
        int countOfWords = 0;

        try (Reader reader = new InputStreamReader(new FileInputStream(args[0]))) {
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

        Sorter.sortByValue(quantity);

        try (Writer writer = new FileWriter("result.csv")) {
            for (Map.Entry<String, Integer> word : quantity.entrySet()) {
                double percent = (double)word.getValue() / countOfWords * 100;
                String result = word.getKey() + ";" + word.getValue() + ";" + percent + "\n";
                //StringBuilder result = new StringBuilder(word.getKey());
                //result.append(";").append(word.getValue()).append(";").append(percent).append("\n");
                writer.write(result);
            }
        } catch (IOException e) {
            System.err.println("Error while writing file: " + e.getLocalizedMessage());
        }
    }
}