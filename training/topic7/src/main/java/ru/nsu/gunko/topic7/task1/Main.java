package ru.nsu.gunko.topic7.task1;

public class Main {
    public static void main(String[] args) {
        Storage<String> storage = new Storage<>(Integer.parseInt(args[0]));

        int countP = Integer.parseInt(args[1]);
        int countC = Integer.parseInt(args[2]);

        Producer p = new Producer(storage);
        Consumer c = new Consumer(storage);

        for (int i = 0; i < countP; ++i) {
            new Thread(p).start();
        }

        for (int i = 0; i < countC; ++i) {
            new Thread(c).start();
        }
    }
}