package com.microwu.concurrent.pcer;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/4   14:30
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class WaitTest {

    static class ThreadA extends Thread {
        public ThreadA(String name) {
            super(name);
        }

        @Override
        public void run() {
            synchronized (this) {
                System.out.println(Thread.currentThread().getName() + " call notify()");
                notify();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadA t1 = new ThreadA("t1");

        synchronized (t1) {
            System.out.println(Thread.currentThread().getName() + " start t1");
            t1.start();

            // 主线程等待t1通过notify()唤醒。
            System.out.println(Thread.currentThread().getName()+" wait()");
            // 导致当前线程等待, 直到另一个线程为该对象调用notify
            t1.wait();

            System.out.println(Thread.currentThread().getName()+" continue");
        }

    }
}