package controller;

import java.util.*;

public class WinController implements Controller {
    private final Object lock = new Object();
    private String line;

    public String getLine() {
        synchronized (lock) {
            while (line == null) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            String result = line;
            line = null;
            return result;
        }
    }

    public List<String> getSettings() {
        return null;
    }

    public void setCommand(String line) {
        synchronized (lock) {
            this.line = line;
            lock.notifyAll();
        }
    }
}
