package ru.nsu.gunko.topic9.task1;

public class Handler implements Runnable {
    private final Printer printer;
    private final CommonRes commonRes;
    private final int M;

    public Handler(int M, Printer printer, CommonRes commonRes) {
        this.printer = printer;
        this.commonRes = commonRes;
        this.M = M;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        int numThread = Integer.parseInt(name.substring(name.length()-1));
        int i = 0;

        while (i < M) {
            if (numThread == commonRes.getThread()) {
                printer.print(numThread);
                ++i;
            } else {
                printer.myWait();
            }
        }
    }
}
