package com.microwu.concurrent.pcer;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *  等待wait 的条件发生了变化
 *  解决办法: 在使用线程的等待/通知机制时, 一般都要在循环中调用wait 方法,
 *          同时注意: while 要放在同步代码块里
 *
 * 总结:
 *  1. wait 使用while循环
 *  2. 使用notifyAll - 多生产多消费情况下, 有可能唤醒的仍然是生产者
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/4   15:41
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConditionChange {
    private static List<String> lockObject = new ArrayList<>();

    public static void main(String[] args) {
        Consumer consumer1 = new Consumer(lockObject);
        Consumer consumer2 = new Consumer(lockObject);
        Producer producer = new Producer(lockObject);
        consumer1.start();
        consumer2.start();
        producer.start();

    }

    static class Consumer extends Thread {
        private List<String> lock;

        public Consumer(List lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                while (lock.isEmpty()) {
                    System.out.println(Thread.currentThread().getName() + " list 为空 ...");
                    System.out.println(Thread.currentThread().getName() + " 调用wait 方法 ...");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " wait 方法结束 ...");

                }
                String remove = lock.remove(0);
                System.out.println(Thread.currentThread().getName() + " 取出第一个元素: " + remove);
            }
        }
    }

    static class Producer extends Thread {
        private List<String> lock;

        public Producer(List lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + " 开始添加元素...");
                lock.add(Thread.currentThread().getName());
                lock.notifyAll();
            };
        }
    }
}