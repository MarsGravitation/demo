package com.microwu.concurrent.executor.ideas;

import java.util.HashSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本线程池存在并发问题，只是简单的把 Dog 的思路用代码实现了
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/26  11:19
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class CustomExecutorV5Copy implements Executor {

    /**
     * 为什么 Dog 这里用 volatile
     * 因为 ThreadPoolExecutor 这些参数可以改变，我这里不能改变
     *
     * 可以改变的话，使用 volatile 保证可见性
     */
    private final int corePoolSize;

    private final int maximumPoolSize;

    private final long keepAliveTime;

    private final TimeUnit unit;

    private final BlockingQueue<Runnable> workQueue;

    private final ThreadFactory threadFactory;

    private final RejectedExecutionHandler handler;

    /**
     * 如果是 core，则线程应该永久存在
     * 如果不是 core，keepAliveTime 后销毁
     * 默认为 false
     */
//    private volatile boolean allowCoreThreadTimeOut;

    private final AtomicInteger workerCount = new AtomicInteger();

    private final HashSet<Worker> workers = new HashSet<>();

    private CustomExecutorV5Copy(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.workQueue = workQueue;
        this.threadFactory = threadFactory;
        this.handler = handler;
    }

    /**
     * execute
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/26     12:06
     *
     * @param command
     * @return void
     */
    @Override
    public void execute(Runnable command) {
        // 如果小于核心线程数，创建新的线程执行任务
        if (workerCount.incrementAndGet() < corePoolSize) {
            addWorker(command, true);
            return;
        }

        // 进入队列
//        if (workQueue.offer(command)) {
//            System.out.println("入队成功");
//        } else if (addWorker(command, false)) {
//            // 队列已满，就创建新的线程执行任务
//            // 超过了最大线程数，执行拒绝策略
//            handler.rejectedExecution(command, null);
//        }
        // 任务入队
        if (!workQueue.offer(command)) {
            // 入队失败
            if (workerCount.incrementAndGet() < maximumPoolSize) {
                // 小于最大线程数，则创建新线程执行任务
                addWorker(command, false);
            } else {
                // 执行拒绝策略
                handler.rejectedExecution(command, null);
            }
        }
    }

    /**
     * addWorker
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/26     12:08
     *
     * @param command
     * @return void
     */
    private boolean addWorker(Runnable command, boolean core) {
        // 这块移到 execute 更好理解
//        if (workCount.incrementAndGet() > (core ? corePoolSize : maximumPoolSize)) {
//            return false;
//        }

        Worker worker = new Worker(command);
        workers.add(worker);
        Thread thread = worker.thread;
        thread.start();
        return true;
    }

    /**
     * getTask
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/26     12:08
     *
     * @param
     * @return java.lang.Runnable
     */
    private Runnable getTask() {
        try {
            // 如果没有超过核心线程，就 take 阻塞
            // 如果超过了核心线程，就超时获取
            return workerCount.get() > corePoolSize ? workQueue.poll(keepAliveTime, unit) : workQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把线程池中线程封装成 Worker
     */
    private final class Worker implements Runnable {

        final Thread thread;

        /**
         * 如果是核心线程，firstTask 不为空，会执行自己的任务
         */
        Runnable firstTask;

        Worker(Runnable firstTask) {
            this.thread = threadFactory.newThread(this);
            this.firstTask = firstTask;
        }

        /**
         * 每个 worker 执行的任务
         *
         * 执行自己的任务，然后从队列中取出任务进行执行
         *
         * @author   chengxudong             chengxd2@lenovo.com
         * @date 2021/6/26     12:09
         *
         * @param
         * @return void
         */
        @Override
        public void run() {
            Runnable task;
            // 取出自己的任务
            task = firstTask;
            // 执行完任务将此任务置空
            firstTask = null;
            // 第一次执行，执行的是自己的任务
            // 后面是从队列中取出来执行
            while (task != null || (task = getTask()) != null) {
                task.run();
                // 将执行完的任务置空
                task = null;
            }

            // getTask 如果小于核心线程数，会阻塞
            // 如果大于核心线程数，就会超时获取，如果超过了指定时间，会返回 null，结束 while 循环
            // 此时线程数 workCount - 1
            workerCount.decrementAndGet();

        }
    }
}
