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
        int T = Integer.parseInt(args[3]);

        ExecutorService serviceP = Executors.newFixedThreadPool(countP);
        ExecutorService serviceC = Executors.newFixedThreadPool(countC);

        List<Future<?>> futuresP = new ArrayList<>();
        List<Future<?>> futuresC = new ArrayList<>();

        CommonRes resP = new CommonRes();
        CommonRes resC = new CommonRes();

        for (int i = 0; i < countP; ++i) {
            futuresP.add(serviceP.submit(new Producer(storage, resP)));
        }

        for (int i = 0; i < countC; ++i) {
            futuresC.add(serviceC.submit(new Consumer(storage, resC)));
        }

        synchronized (Thread.currentThread()) {
            try {
                Thread.currentThread().wait(T * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        serviceP.shutdown();
        resP.setFlag(false);

        while (true) {
            if (Check.start(futuresP)) {
                synchronized (Thread.currentThread()) {
                    try {
                        Thread.currentThread().wait(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                System.out.println("Producer's are finished");
                break;
            }
        }

        while (true) {
            if (!storage.isEmpty()) {
                synchronized (Thread.currentThread()) {
                    try {
                        Thread.currentThread().wait(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                System.out.println("Storage is empty");
                break;
            }
        }

        serviceC.shutdown();
        resC.setFlag(false);

        if (Check.start(futuresC)) {
            try {
                if (!serviceC.awaitTermination(10, TimeUnit.SECONDS)) {
                    serviceC.shutdownNow();
                    if (!serviceC.awaitTermination(10, TimeUnit.SECONDS)) {
                        System.err.println("Pool did not terminate");
                    }
                }
            } catch (InterruptedException ex) {
                serviceC.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Consumer's are finished");
    }
}
