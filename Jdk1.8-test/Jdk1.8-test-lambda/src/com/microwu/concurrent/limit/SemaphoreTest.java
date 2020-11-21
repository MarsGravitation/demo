package com.microwu.concurrent.limit;

import java.util.concurrent.Semaphore;

/**
 * Description: 信号量限流
 *
 * https://houbb.github.io/2018/12/23/ha-limit-03-semaphore-in-java
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/15   17:42
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        LimitRunnable runnable = new LimitRunnable();
        new Thread(runnable).start();
        new Thread(runnable).start();
    }

    private static class LimitSemaphore {
        private final Semaphore semaphore;

        public LimitSemaphore() {
            this.semaphore = new Semaphore(1);
        }

        public synchronized void acquire() {
            try {
                this.semaphore.acquire(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void release() {
            this.semaphore.release(1);
        }
    }

    private static class LimitRunnable implements Runnable {

        private final LimitSemaphore limitSemaphore = new LimitSemaphore();

        @Override
        public void run() {
            for (int i = 0; i < 2; i++) {
                limitSemaphore.acquire();
                System.out.println(System.currentTimeMillis()/1000 + ": " + Thread.currentThread().getName() + " - " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                limitSemaphore.release();
            }
        }
    }

}