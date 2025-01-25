package ru.nsu.gunko.topic9.task1;

public class Printer {
    private final CommonRes commonRes;
    private final int N;

    public Printer(int N, CommonRes commonRes) {
        this.commonRes = commonRes;
        this.N = N;
    }

    public synchronized void print(int numThread) {
        System.out.println(numThread+"-"+commonRes.getString());

        synchronized (commonRes) {
            if (numThread == N) {
                commonRes.begThread();
                commonRes.addString();
            } else {
                commonRes.addThread();
            }
        }

        notifyAll();
    }

    public synchronized void myWait() {
        try {
            wait(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
