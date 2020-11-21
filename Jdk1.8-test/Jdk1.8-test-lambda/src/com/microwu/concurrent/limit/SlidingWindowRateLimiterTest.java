package com.microwu.concurrent.limit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Description: 滑动窗口
 *
 * https://cloud.tencent.com/developer/article/1359889
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/16   14:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SuppressWarnings("Duplicates")
public class SlidingWindowRateLimiterTest {

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter();
        executor.scheduleAtFixedRate(limiter, 1, 1, TimeUnit.SECONDS);
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                while (true) {
                    boolean l = limiter.visit();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    private static class SlidingWindowRateLimiter implements Runnable {

        private final long second = 2;
        private final int block = 10;
        private final AtomicLong[] countPerBlock = new AtomicLong[block];

        private AtomicLong count;
        private volatile int index;

        public SlidingWindowRateLimiter() {
            for (int i = 0; i < block; i++) {
                countPerBlock[i] = new AtomicLong();
            }
            count = new AtomicLong(0);

        }

        public boolean isOverLimit() {
            return currentQps() > second;
        }

        public long currentQps() {
            return count.get();
        }

        public synchronized boolean visit() {
            countPerBlock[index].incrementAndGet();
            count.incrementAndGet();
            System.out.println(System.currentTimeMillis() / 1000 + ": " + Thread.currentThread().getName() + " - " + isOverLimit());
            return isOverLimit();
        }

        @Override
        public void run() {
            index = (index + 1) % block;
            long val = countPerBlock[index].getAndSet(0);
            count.addAndGet(-val);

        }
    }
}