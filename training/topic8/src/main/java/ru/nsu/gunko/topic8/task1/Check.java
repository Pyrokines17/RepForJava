package ru.nsu.gunko.topic8.task1;

import java.util.List;
import java.util.concurrent.Future;

public class Check {
    public static boolean start(List<Future<?>> futures) {
        for (Future<?> future : futures) {
            if (!future.isDone()) {
                return true;
            }
        }
        return false;
    }
}
