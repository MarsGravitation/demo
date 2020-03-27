package com.microwu.concurrent.pcer;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/4   14:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class WaitTimeTest implements Runnable {
     private static final Object object = new Object();
    @Override
    public void run() {
        synchronized (object) {
            System.out.println(Thread.currentThread().getName() + " Start ...");
            try {
                object.wait(5000);
                System.out.println(Thread.currentThread().getName() + " continue ...");
                System.out.println(Thread.currentThread().getName() + " end ...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        WaitTimeTest waitTimeTest = new WaitTimeTest();
        Thread thread = new Thread(waitTimeTest);
        thread.start();
        TimeUnit.SECONDS.sleep(3);
        synchronized (object) {
            System.out.println(Thread.currentThread().getName() + " start ... ");
            TimeUnit.SECONDS.sleep(3);
            System.out.println(Thread.currentThread().getName() + " end ... ");
        }
    }
}