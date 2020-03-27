package com.microwu.cxd.multithreading.sync;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/14   14:57
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SynchronizedDemo implements Runnable {
    // 共享资源 - 临界资源
    private static int i = 0;

    public void increase() {
        i++;
    }
    @Override
    public void run() {
        for(int i = 0; i < 10000; i++) {
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
        Thread t1 = new Thread(synchronizedDemo);
        Thread t2 = new Thread(synchronizedDemo);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);

    }
}