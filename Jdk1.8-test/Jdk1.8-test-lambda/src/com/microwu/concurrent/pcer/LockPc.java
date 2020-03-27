package com.microwu.concurrent.pcer;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/4   16:17
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LockPc {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition full = lock.newCondition();
    private static Condition empty = lock.newCondition();

    public static void main(String[] args) {
        LinkedList<Integer> integers = new LinkedList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(15);
        for (int i = 0; i < 5; i++) {
            threadPool.submit(new Producer(integers, 8, lock));
        }

        for (int i = 0; i < 10; i++) {
            threadPool.submit(new Consumer(integers, lock));
        }
    }

    static class Producer implements Runnable {
        private List<Integer> list;
        private int maxLength;
        private Lock lock;

        public Producer(List list, int maxLength, Lock lock) {
            this.list = list;
            this.maxLength = maxLength;
            this.lock = lock;
        }

        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (list.size() == maxLength) {
                        System.out.println("生产者 " + Thread.currentThread().getName() + "停止生产 ...");

                        full.await();
                    }
                    int i = new Random().nextInt();
                    list.add(i);
                    System.out.println("生产者 " + Thread.currentThread().getName() + " 生产数据 " + i);
                    empty.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private List<Integer> list;
        private Lock lock;

        public Consumer(List list, Lock lock) {
            this.list = list;
            this.lock = lock;
        }

        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    if (list.isEmpty()) {
                        System.out.println("消费者: " + Thread.currentThread().getName() + "没有数据了...");

                        empty.await();
                    }
                    System.out.println("消费者" + Thread.currentThread().getName() + "消费数据" + list.remove(0));
                    full.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}