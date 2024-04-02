package ru.nsu.gunko.topic8.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        int count = Integer.parseInt(args[0]);
        BlockingDeque<String> storage = new LinkedBlockingDeque<>(count);

        int countP = Integer.parseInt(args[1]);
        int countC = Integer.parseInt(args[2]);

        Producer p = new Producer(storage);
        Consumer c = new Consumer(storage);

        List<Future<?>> futures = new ArrayList<>();

        try (ExecutorService service = Executors.newCachedThreadPool()) {
            for (int i = 0; i < countP; ++i) {
                futures.add(service.submit(p));
            }

            for (int i = 0; i < countC; ++i) {
                futures.add(service.submit(c));
            }

            synchronized (Thread.currentThread()) {
                Thread.currentThread().wait(1000);
            }

            if (Check.start(futures)) {
                service.shutdownNow();
            }
        } catch (InterruptedException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }
}