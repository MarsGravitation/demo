package com.microwu.concurrent.pcer;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/4   14:59
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class WaitTest3 implements Runnable {
    private static final Object object = new Object();
    private static boolean condition = true;

    @Override
    public void run() {
        synchronized (object) {
            try {
                while (condition) {
                    object.wait();
                }
                System.out.println(Thread.currentThread().getName() + " condition " + condition);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new WaitTest3()).start();

        TimeUnit.SECONDS.sleep(2);

        synchronized (object) {
            object.notify();
            condition = false;
        }

    }
}