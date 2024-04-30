package ru.nsu.gunko.threads;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class CustomPool implements ExecutorService {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition terminate = lock.newCondition();

    private final List<ThreadWorker> workers;
    private final int sizeOfPool;
    private final BlockingQueue<Runnable> tasks;

    private final static int THR_TIME = 1000;
    private volatile boolean isShutdown = false;

    public CustomPool(int size, BlockingQueue<Runnable> queue) {
        this.workers = new ArrayList<>(size);
        this.sizeOfPool = size;
        this.tasks = queue;
    }

    private class ThreadWorker extends Thread {
        @Override
        public void run() {
            try {
                while (!isShutdown && !Thread.currentThread().isInterrupted()) {
                    Runnable task = tasks.poll(THR_TIME, TimeUnit.MILLISECONDS);

                    if (task == null) {
                        break;
                    } else {
                        task.run();
                    }
                }
            } catch (InterruptedException ignored) {
            } finally {
                finishWorker(this);
            }
        }
    }

    private void finishWorker(ThreadWorker worker) {
        lock.lock();
        try {
            workers.remove(worker);
            terminate.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection)
            throws InterruptedException {

        if (isShutdown) {
            throw new RejectedExecutionException();
        }

        List<Future<T>> futures = prepare(collection);

        try {
            for (Future<T> future : futures) {
                if (!future.isDone()) {
                    try {
                        future.get();
                    } catch (ExecutionException | CancellationException ignored) {
                    }
                }
            }
        } finally {
            for (Future<T> future : futures) {
                future.cancel(true);
            }
        }

        return futures;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit)
            throws InterruptedException {

        long timeoutMillis = timeUnit.toMillis(l);
        long startTime = System.currentTimeMillis();
        long endTime = startTime+timeoutMillis;

        List<Future<T>> futures = prepare(collection);

        try {
            for (Future<T> future : futures) {
                try {
                    long timeLeft = endTime - System.currentTimeMillis();

                    if (!future.isDone()) {
                        try {
                            future.get(timeLeft, TimeUnit.MILLISECONDS);
                        } catch (TimeoutException e) {
                            break;
                        }
                    }
                } catch (ExecutionException | CancellationException ignored) {
                }
            }
        } finally {
            for (Future<T> future : futures) {
                future.cancel(true);
            }
        }

        return futures;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> collection)
            throws InterruptedException, ExecutionException {

        if (isShutdown) {
            throw new RejectedExecutionException();
        }

        List<Future<T>> futures = prepare(collection);

        try {
            for (Future<T> future : futures) {
                if (future.isDone()) {
                    return future.get();
                }
            }
        } finally {
            for (Future<T> future : futures) {
                future.cancel(true);
            }
        }

        throw new RuntimeException();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit)
            throws InterruptedException, ExecutionException, TimeoutException {

        long timeoutMillis = timeUnit.toMillis(l);
        long startTime = System.currentTimeMillis();
        long endTime = startTime+timeoutMillis;

        List<Future<T>> futures = prepare(collection);

        try {
            for (Future<T> future : futures) {
                long timeLeft = endTime - System.currentTimeMillis();

                if (timeLeft > 0) {
                    return future.get(timeLeft, TimeUnit.MILLISECONDS);
                }
            }
        } finally {
            for (Future<T> future : futures) {
                future.cancel(true);
            }
        }

        throw new TimeoutException();
    }

    private <T> List<Future<T>> prepare(Collection<? extends Callable<T>> collection) {
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

        for (ThreadWorker worker : workers) {
            worker.interrupt();
        }

        return new ArrayList<>(tasks);
    }

    @Override
    public boolean isShutdown() {
        return isShutdown;
    }

    @Override
    public boolean isTerminated() {
        if (!isShutdown) {
            return false;
        }

        for (ThreadWorker worker : workers) {
            if (worker.isAlive()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit)
            throws InterruptedException {

        long timeoutMillis = timeUnit.toMillis(l);
        long startTime = System.currentTimeMillis();
        long endTime = startTime+timeoutMillis;

        lock.lock();
        try {
            long curTime = System.currentTimeMillis();
            while (!workers.isEmpty() && curTime < endTime) {
                terminate.await(endTime-curTime, TimeUnit.MILLISECONDS);
                curTime = System.currentTimeMillis();
            }
        } finally {
            lock.unlock();
        }

        return isTerminated();
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        if (callable == null) {
            throw new NullPointerException();
        }

        if (isShutdown) {
            throw new RejectedExecutionException();
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

        if (isShutdown) {
            throw new RejectedExecutionException();
        }

        FutureTask<T> futureTask = new FutureTask<>(runnable, t);
        execute(futureTask);

        return futureTask;
    }

    @Override
    public Future<?> submit(Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }

        if (isShutdown) {
            throw new RejectedExecutionException();
        }

        FutureTask<Void> futureTask = new FutureTask<>(task, null);
        execute(futureTask);

        return futureTask;
    }

    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException();
        }

        if (isShutdown || !tasks.offer(runnable)) {
            throw new RejectedExecutionException();
        }

        if (!tasks.isEmpty() && workers.size() < sizeOfPool) {
            ThreadWorker worker = new ThreadWorker();
            worker.start();
            workers.add(worker);
        }
    }
}