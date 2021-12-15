package com.microwu.concurrent.executor.ideas;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/24  17:52
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class CustomExecutorV3 implements Executor {

    private final BlockingQueue<Runnable> workQueue;

    public CustomExecutorV3(int corePoolSize, BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        for (int i = 0; i < corePoolSize; i++) {
            new Thread(new Worker()).start();
        }
    }

    @Override
    public void execute(Runnable command) {
        if (!workQueue.offer(command)) {
            System.out.println("任务队列已满，丢弃任务！");
        }
    }

    @SuppressWarnings("all")
    private final class Worker implements Runnable {

        @Override
        public void run() {
            Runnable task;
            while(!Thread.currentThread().isInterrupted()) {
                if ((task = workQueue.poll()) != null) {
                    task.run();
                } else {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
