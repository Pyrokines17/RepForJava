package ru.nsu.gunko.topic9.task1;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]),
                M = Integer.parseInt(args[1]);
        CommonRes commonRes = new CommonRes();
        Printer printer = new Printer(N, commonRes);

        try (ExecutorService service = Executors.newCachedThreadPool()) {
            for (int i = 0; i < N; ++i) {
                service.execute(new Handler(M, printer, commonRes));
            }
        }
    }
}