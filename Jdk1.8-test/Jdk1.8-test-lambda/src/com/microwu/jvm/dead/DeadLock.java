package com.microwu.jvm.dead;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/3   14:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DeadLock {
    public static void main(String[] args) {
        System.out.println("start the example ...");
        final Object object1 = new Object();
        final Object object2 = new Object();

        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                synchronized (object1) {
                    try {
                        System.out.println("Thread t1 start ...");
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                    synchronized (object2) {
                        System.out.println("Thread t1 done ....");
                    }
                }
            }
        };

        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                synchronized (object2) {
                    try {
                        System.out.println("thread t2 start ....");
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                    synchronized (object1) {
                        System.out.println("thread t2 done");
                    }
                }
            }
        };

        t1.start();
        t2.start();
    }
}