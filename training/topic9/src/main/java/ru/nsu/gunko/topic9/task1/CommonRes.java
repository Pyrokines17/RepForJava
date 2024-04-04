package ru.nsu.gunko.topic9.task1;

public class CommonRes {
    private int thread;
    private int string;

    public CommonRes() {
        thread = 1;
        string = 1;
    }

    public int getString() {
        return string;
    }

    public int getThread() {
        return thread;
    }

    public void addString() {
        ++string;
    }

    public void addThread() {
        ++thread;
    }

    public void begThread() {
        thread = 1;
    }
}
