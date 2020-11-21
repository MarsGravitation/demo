package com.microwu.concurrent.limit;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 固定窗口
 *  没进来一个请求，计数器 + 1
 *  当计数器达到上限时，则触发限流
 *  时间每经过 1 秒，重置计数器
 *
 *  这里用 CountDownLatch 和 ReentrantLock 效果一样，我认为的
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/16   11:13
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LimitFixedWindowTest {

    public static void main(String[] args) {
        new Thread(new LimitRunnable()).start();
        new Thread(new LimitRunnable()).start();
    }

    private static class LimitFixedWindow {

        private AtomicInteger counter = new AtomicInteger(0);

        /**
         * 避免不同线程的 notify + wait 报错问题
         */
        private CountDownLatch latch = new CountDownLatch(1);

        public LimitFixedWindow() {
            Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
                initCounter();
            }, 1, 1, TimeUnit.SECONDS);
        }

        public synchronized void acquire() {
            while (counter.get() >= 1) {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            this.counter.incrementAndGet();
            this.latch = new CountDownLatch(1);
        }

        private void initCounter() {
            if (this.counter.get() >= 1) {
                this.counter = new AtomicInteger(0);
                // 这里不能无脑 notify 会卡住
                latch.countDown();
            } else {
                this.counter = new AtomicInteger(0);
            }
        }
    }

    private static class LimitRunnable implements Runnable {

        // 静态变量，多个对象共享一个变量
        private static final LimitFixedWindow limit = new LimitFixedWindow();

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                limit.acquire();
                System.out.println(System.currentTimeMillis()/1000 + ": " + Thread.currentThread().getName() + " - " + i);
            }
        }
    }
}