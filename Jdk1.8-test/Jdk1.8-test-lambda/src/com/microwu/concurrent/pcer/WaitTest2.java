package com.microwu.concurrent.pcer;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/4   14:55
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class WaitTest2 implements Runnable {
    private static final Object obj = new Object();

    @Override
    public void run() {
        synchronized (obj) {
            System.out.println(Thread.currentThread().getName()+" start");
            try {
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" continue");
            System.out.println(Thread.currentThread().getName()+" end");
        }
    }

    /**
     * 主线程必须等待所有的子线程死亡才会结束
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/12/4  14:58
     *
     * @param   	args
     * @return  void
     */
    public static void main(String[] args) throws InterruptedException {
        WaitTest2 waitTest2 = new WaitTest2();
        Thread t1 = new Thread(waitTest2);
        Thread t2 = new Thread(waitTest2);
        Thread t3 = new Thread(waitTest2);

        t1.start();
        t2.start();
        t3.start();

        TimeUnit.SECONDS.sleep(2);
        synchronized (obj) {
            System.out.println(Thread.currentThread().getName()+" start");
            System.out.println(Thread.currentThread().getName()+" notify");
            obj.notify();
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName()+" end");
        }

    }
}