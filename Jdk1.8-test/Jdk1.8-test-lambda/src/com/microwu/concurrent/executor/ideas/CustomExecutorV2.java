package com.microwu.concurrent.executor.ideas;

import com.microwu.inner.Worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池 V2
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/24  17:41
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class CustomExecutorV2 implements Executor {

    private final BlockingQueue<Runnable> queue;

    public CustomExecutorV2(BlockingQueue<Runnable> queue) {
        this.queue = queue;
        new Thread(new Worker()).start();
    }

    @Override
    public void execute(Runnable command) {
        if (!queue.offer(command)) {
            System.out.println("任务队列已满，丢弃任务！");
        }
    }

    @SuppressWarnings("all")
    private final class Worker implements Runnable {
        @Override
        public void run() {
            Runnable task;
            while (!Thread.currentThread().isInterrupted()) {
                if ((task = queue.poll()) != null) {
                    task.run();
                } else {
                    // 防止空转
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
