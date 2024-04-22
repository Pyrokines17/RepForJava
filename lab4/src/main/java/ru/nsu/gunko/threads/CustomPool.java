package ru.nsu.gunko.threads;

import java.util.*;
import java.util.concurrent.*;

public class CustomPool implements ExecutorService {
    private final Queue<Runnable> tasks;
    private boolean isShutdown;

    public CustomPool(int size, Queue<Runnable> queue) {
        isShutdown = false;
        tasks = queue;

        for (int i = 0; i < size; ++i) {
            new ThreadWorker().start();
        }
    }

    private class ThreadWorker extends Thread {
        @Override
        public void run() {
            while (!isShutdown) {
                Runnable task;

                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException exception) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    task = tasks.poll();
                }

                if (task != null) {
                    task.run();
                }
            }
        }
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection)
        throws InterruptedException {
        return invokeAll(collection, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit)
        throws InterruptedException {
        List<Future<T>> futures = prepare(collection, l, timeUnit);
        long timeoutMillis = timeUnit.toMillis(l);
        long startTime = System.currentTimeMillis();

        while (true) {
            boolean isDone = true;

            for (Future<T> future : futures) {
                if (!future.isDone()) {
                    isDone = false;
                    break;
                }
            }

            if (isDone) {
                return futures;
            }

            if (timeoutMillis > 0) {
                long now = System.currentTimeMillis();
                if (now - startTime >= timeoutMillis) {
                    return futures;
                }
            }

            Thread.currentThread().wait(100);
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> collection)
            throws InterruptedException, ExecutionException {
        return invokeAny(collection, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit)
    throws InterruptedException, ExecutionException {
        List<Future<T>> futures = prepare(collection, l, timeUnit);
        long timeoutMillis = timeUnit.toMillis(l);
        long startTime = System.currentTimeMillis();

        while (true) {
            for (Future<T> future : futures) {
                if (future.isDone()) {
                    return future.get();
                }
            }

            if (timeoutMillis > 0) {
                long now = System.currentTimeMillis();
                if (now - startTime >= timeoutMillis) {
                    throw new RuntimeException();
                }
            }

            Thread.currentThread().wait(100);
        }
    }

    private <T> List<Future<T>> prepare(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) {
        if (collection == null || timeUnit == null) {
            throw new NullPointerException();
        }

        if (collection.isEmpty() || l < 0) {
            throw new IllegalArgumentException();
        }

        List<Future<T>> futures = new ArrayList<>();

        for (Callable<T> callable : collection) {
            futures.add(submit(callable));
        }

        return futures;
    }

    @Override
    public void shutdown() {
        isShutdown = true;
    }

    @Override
    public List<Runnable> shutdownNow() {
        isShutdown = true;

        List<Runnable> curTasks = new ArrayList<>(tasks);
        tasks.clear();

        return curTasks;
    }

    @Override
    public boolean isShutdown() {
        return isShutdown;
    }

    @Override
    public boolean isTerminated() {
        return isShutdown && tasks.isEmpty();
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit) {
        long timeoutMillis = timeUnit.toMillis(l);
        long startTime = System.currentTimeMillis();

        while (!isTerminated()) {
            try {
                Thread.currentThread().wait(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (System.currentTimeMillis()-startTime > timeoutMillis) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Future<?> submit(Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }

        FutureTask<Void> futureTask = new FutureTask<>(task, null);
        execute(futureTask);
        return futureTask;
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        if (callable == null) {
            throw new NullPointerException();
        }

        FutureTask<T> futureTask = new FutureTask<>(callable);
        execute(futureTask);
        return futureTask;
    }

    @Override
    public <T> Future<T> submit(Runnable runnable, T t) {
        if (runnable == null) {
            throw new NullPointerException();
        }

        FutureTask<T> futureTask = new FutureTask<>(runnable, t);
        execute(futureTask);
        return futureTask;
    }

    @Override
    public void execute(Runnable runnable) {
        if (isShutdown) {
            throw  new IllegalStateException("ExecutorService is shutdown");
        }

        synchronized (tasks) {
            tasks.add(runnable);
            tasks.notify();
        }
    }
}
