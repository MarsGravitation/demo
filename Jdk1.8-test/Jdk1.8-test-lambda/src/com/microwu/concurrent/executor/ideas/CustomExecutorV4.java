package com.microwu.concurrent.executor.ideas;

import java.util.HashSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/24  18:05
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class CustomExecutorV4 implements Executor {

    /**
     * 核心线程数量
     */
    private final int corePoolSize;

    /**
     *
     */
    private final AtomicInteger workerCount = new AtomicInteger();

    private final BlockingQueue<Runnable> workQueue;

    private final ThreadFactory threadFactory;

    private final RejectedExecutionHandler rejectedExecutionHandler;

    /**
     * 线程的引用
     * 可用于中断线程
     */
    private final HashSet<Worker> workers = new HashSet<>();

    public CustomExecutorV4(int corePoolSize, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        this.corePoolSize = corePoolSize;
        this.workQueue = workQueue;
        this.threadFactory = threadFactory;
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }

    @Override
    public void execute(Runnable command) {
        if (workerCount.getAndIncrement() <= corePoolSize) {
            Worker worker = new Worker(command);
            workers.add(worker);
            worker.thread.start();
        } else if (!workQueue.offer(command)) {
            rejectedExecutionHandler.rejectedExecution(command, null);
        }

    }

    @SuppressWarnings("all")
    private final class Worker implements Runnable {

        final Thread thread;
        private Runnable task;

        Worker(Runnable firstTask) {
            this.task = firstTask;
            this.thread = threadFactory.newThread(this);
        }

        @Override
        public void run() {
            while (task != null || (task = workQueue.poll()) != null) {
                task.run();
                task = null;
            }
        }
    }
}
