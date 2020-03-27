package com.microwu.concurrent.pcer;

import java.util.concurrent.TimeUnit;

/**
 * Description: 过早的唤醒,
 * 解决办法: 添加一个状态标志位
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/4   9:57
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class EarlyNotify {
    private static Object lockObject = new Object();
    private static volatile boolean isWait = false;

    public static void main(String[] args) throws InterruptedException {
        Thread notify = new NotifyThread(lockObject);
        Thread wait = new WaitThread(lockObject);

        notify.start();
        wait.start();
//        TimeUnit.SECONDS.sleep(1);



    }

    static class WaitThread extends Thread {
        private Object lock;

        public WaitThread(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                try {
                    System.out.println(Thread.currentThread().getName() + "进入代码块 ...");
                    System.out.println(Thread.currentThread().getName() + "开始wait ...");
                    isWait = true;
                    lock.wait();
                    System.out.println(Thread.currentThread().getName() + " 结束wait ... ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class NotifyThread extends Thread {
        private Object lock;

        public NotifyThread(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock) {
                    if (isWait) {
                        System.out.println(Thread.currentThread().getName() + "进入代码块 ...");
                        System.out.println(Thread.currentThread().getName() + "开始notify ...");
                        lock.notifyAll();
                        System.out.println(Thread.currentThread().getName() + " 结束notify ... ");
                    }
                }
            }
        }
    }
}