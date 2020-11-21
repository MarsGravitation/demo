package com.microwu.concurrent.utils;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 *  CountDownLatch：经常用于监听某些初始化操作，等初始化执行完毕，通知主线程继续工作
 *      联盟开始时，要等所有玩家都就绪，游戏才可以开始
 *
 *  CyclicBarrier：
 *      每个线程代表一个跑步运动员，当运动员都准备好后，才一起出发，只要有一个没准备，大家都等待
 *
 *  Future 模式：
 *
 *  Semaphore：
 *      信号量非常适合高并发访问
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/19   16:59
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class UtilsTest {

    private final static CountDownLatch latch = new CountDownLatch(2);

    public static void test() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " 开始执行。。。");
        new CountDownLatchTest().start();
        new CountDownLatchTest().start();
        latch.await();
        System.out.println(Thread.currentThread().getName() + " 执行完毕。。。");

    }

    public static void test02() {
        CyclicBarrier barrier = new CyclicBarrier(2);
        ExecutorService pool = Executors.newFixedThreadPool(2);

        pool.submit(new Runner(barrier, "Thread-1"));
        pool.submit(new Runner(barrier, "Thread-2"));

        pool.shutdown();
    }

    public static void test03() {
        ExecutorService pool = Executors.newCachedThreadPool();
        // 同时只能 5 个线程访问
        final Semaphore semaphore = new Semaphore(5);
        // 模拟 20 个客户端访问
        for (int i = 0; i < 20; i++) {
            pool.submit(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("acquire: " + Thread.currentThread().getName());
                    Thread.sleep((long) (Math.random() * 10000));
                    semaphore.release();
                    System.out.println("release: " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        pool.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
//        test();
//        test02();
        test03();
    }

    private static class CountDownLatchTest extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            System.out.println(Thread.currentThread().getName() + " 执行完毕。。。");
        }
    }

    private static class Runner implements Runnable {

        private CyclicBarrier barrier;
        private String name;

        public Runner(CyclicBarrier barrier, String name) {
            this.barrier = barrier;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                System.out.println(name + "准备 OK !");
                Thread.sleep(1000 * (new Random().nextInt(5)));
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(name + " Go !");
        }
    }

    /**
     * 1. ReentrantLock 一般用法：
     *  private Lock lock = new ReentrantLock();
     * try {
     *     lock.lock();
     *     //do something
     * } finally {
     *     lock.unlock();
     * }
     *
     * 2. condition 使用方法，注意 condition 可以实例化多个
     *
     *  Lock lock = new ReentrantLock();
     * Condition condition = lock.newCondition();
     * condition.await(); //阻塞线程，释放锁
     * condition.signal();//唤醒线程，不释放锁
     *
     * 3. 公平锁和非公平锁，非公平锁效率比公平锁高
     *  Lock lock = new ReentrantLock(boolean isFair);
     *  true - 公平，false - 非公平
     *
     * 4. 读写锁，实现读写分离的锁，适用于读多写少的情况下，读读共享，读写互斥
     *  private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock(); // (1)
     * private ReadLock readLock = rwlock.readLock();    // (2)
     * private WriteLock writeLock = rwlock.writeLock(); // (3)
     *
     * public void read(){
     *     try {
     *         readLock.lock();
     *         // do something
     *     } finally {
     *         readLock.unlock();
     *     }
     * }
     *
     * public void write(){
     *     try {
     *         writeLock.lock();
     *         // do something
     *     } finally {
     *         writeLock.unlock();
     *     }
     * }
     */
    private static class ReentrantLockTest implements Runnable {
        private Lock lock = new ReentrantLock();

        @Override
        public void run() {
            lock.lock();
            try {
                // do something
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                lock.unlock();
            }
        }
    }
}