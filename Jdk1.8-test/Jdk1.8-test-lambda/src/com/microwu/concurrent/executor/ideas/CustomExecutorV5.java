package com.microwu.concurrent.executor.ideas;

import java.time.Instant;
import java.util.HashSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/24  19:52
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class CustomExecutorV5 implements Executor {

    /**
     * 这些参数没用 volatile，因为原版是可以修改的，使用 volatile 保证可见性
     *
     * 这里我直接使用 final，保证不变
     */
    private final int corePoolSize;

    private final int maximumPoolSize;

    private final long keepAliveTime;

    private final AtomicInteger workCount = new AtomicInteger();

    private final BlockingQueue<Runnable> workQueue;

    private final ThreadFactory threadFactory;

    private final RejectedExecutionHandler rejectedExecutionHandler;

    private final HashSet<Worker> workers = new HashSet<>();

    public CustomExecutorV5(int corePoolSize, int maximumPoolSize, long keepAliveTime, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.workQueue = workQueue;
        this.threadFactory = threadFactory;
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }

    @Override
    public void execute(Runnable command) {
        if (workCount.get() < corePoolSize) {
//            Thread thread = threadFactory.newThread(new Worker(true, command));
            addWorker(command);
            return;
        }
        if (!workQueue.offer(command)) {
            if (workCount.get() < maximumPoolSize) {
//                Thread thread = threadFactory.newThread(new Worker(false, command));
                addWorker(command);
                return;
            }
            rejectedExecutionHandler.rejectedExecution(command, null);
        }

    }

    private void addWorker(Runnable command) {
        Worker worker = new Worker(command);
        workCount.getAndIncrement();
        workers.add(worker);
        worker.thread.start();
    }

    @SuppressWarnings("all")
    private final class Worker implements Runnable {

        final Thread thread;
        Runnable task;

        Worker(Runnable firstTask) {
            this.task = firstTask;
            thread = threadFactory.newThread(this);
        }

        @Override
        public void run() {
            while (task != null || (task = getTask()) != null) {
                task.run();
                task = null;
            }
            workCount.getAndIncrement();
        }

        /**
         * 阻塞式的从队列中获取任务
         *
         * @author   chengxudong             chengxd2@lenovo.com
         * @date 2021/6/25     10:58
         *
         * @param
         * @return java.lang.Runnable
         */
        private Runnable getTask() {
            boolean timed = workCount.get() > corePoolSize;
            try {
                return timed ? workQueue.poll(keepAliveTime, TimeUnit.MILLISECONDS) : workQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
