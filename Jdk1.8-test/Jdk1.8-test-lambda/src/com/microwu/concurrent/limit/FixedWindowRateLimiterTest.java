package com.microwu.concurrent.limit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 *
 * https://cloud.tencent.com/developer/article/1359889
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/16   14:07
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SuppressWarnings("Duplicates")
public class FixedWindowRateLimiterTest {

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        FixedWindowRateLimiter limiter = new FixedWindowRateLimiter();
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

    private static class FixedWindowRateLimiter implements Runnable {

//        private static final int visit = 3;

        private final int second = 2;

        private AtomicInteger count = new AtomicInteger();

        public int currentQps() {
            return count.get();
        }

        /**
         * false 不限
         * true  限
         *
         * @author   chengxudong               chengxudong@microwu.com
         * @date    2020/10/16  14:24
         *
         * @param
         * @return  boolean
         */
        public boolean isOverLimit() {
            return currentQps() > second;
        }

        public synchronized boolean visit() {
            count.incrementAndGet();
            System.out.println(System.currentTimeMillis() / 1000 + ": " + Thread.currentThread().getName() + " - " + isOverLimit());
            return isOverLimit();
        }

        @Override
        public void run() {
            count.set(0);
        }
    }

}