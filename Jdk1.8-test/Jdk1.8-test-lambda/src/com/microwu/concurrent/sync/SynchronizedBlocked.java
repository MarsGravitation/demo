package com.microwu.concurrent.sync;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * 线程中断操作对于正在等待获取的锁对象的synchronized 方法或者代码块不起作用
 *
 * 等待唤醒机制
 *  wait/notify 必须处于synchronized, 因为调用这几个方法之前必须拿到当前对象的monitor
 *  而synchronized 可以获取
 *
 *  wait 和 sleep 区别: wait 会释放锁, 而sleep 不会释放锁
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/2   17:19
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SynchronizedBlocked implements Runnable {

    public SynchronizedBlocked() {
        new Thread() {
            @Override
            public void run() {
                f();
            }
        }.start();
    }

    public synchronized void f() {
        System.out.println("Trying to call f()");
        while (true) {
            Thread.yield();
        }
    }

    @Override
    public void run() {
        while(true) {
            if(Thread.interrupted()) {
                System.out.println("中断线程");
                break;
            } else {
                f();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedBlocked synchronizedBlocked = new SynchronizedBlocked();
        Thread thread = new Thread(synchronizedBlocked);
        thread.start();
        TimeUnit.SECONDS.sleep(2);
        thread.interrupt();
    }
}