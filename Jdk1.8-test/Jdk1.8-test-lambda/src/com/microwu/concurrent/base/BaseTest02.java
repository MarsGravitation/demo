package com.microwu.concurrent.base;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description: 并发编程解决方案 - 信号量 VS 管程
 *  1.1 相关概念
 *      1. 临界资源：虽然多个进程可以共享系统中的各种资源，但其中很多资源一次只能为一个进程所用，
 *          我们把一次仅允许一个进程使用的临界资源称为临界资源。
 *      2. 临界区：对临界资源的访问，必须互斥地进行，在每个进程中，访问临界资源的那段代码称为临界区
 *      3. 互斥：只有一个线程能访问临界区
 *
 *  1.2 信号量 VS 管程
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/16   10:04
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BaseTest02 {

    public static void main(String[] args) {

    }

    /**
     * 信号量：将共享变量封装起来，对共享变量的所有操作只能通过 PV 进行；封装共享变量是并发编程的常用手段
     *  1. 互斥访问
     *  实现临界区的互斥访问的注意事项：信号量的初始值必须为 1；PV 必须配对使用
     *  Semaphore mutex = new Semaphore(1);
     *  mutex.P();
     *  // do something
     *  mutex.V();
     *
     *  2. 条件访问
     *  Semaphore condition = new Semaphore(0);
     *  // Thread A 进行等待
     *  condition.P();
     *
     *  // Thread B 唤醒
     *  condition.V();
     *  注意事项：初始信号量必须为 0，这样所有的线程调用 P 操作都无法获取到锁，只能等待线程 B 调用 V 唤醒
     *
     */
    private static class Semaphore {
        private int sem;
        private WaitQueue queue;

        public Semaphore(int sem) {
            this.sem = sem;
        }

        void P() {
            sem--;
            if (sem < 0) {
                // add this thread t to q
//                block(t);
            }
        }

        void V() {
            sem++;
            if (sem <= 0) {
                // remove a thread t from q
//                wakeup(t);
            }
        }

        class WaitQueue {

        }
    }

    /**
     * 1. 任何时候只能有一个线程操作缓存区：互斥访问，使用二进制信号量 mutex，其信号初始值为 1
     * 2. 缓存区空时，消费者必须等待生产者：条件同步，使用资源信号量 notEmpty，信号初始值为 0
     * 3. 缓存区满时，生产者必须等待消费者：条件同步，使用资源信号量 notFull，信号初始值为 n
     */
    private static class BounderBuffer {
        private int n = 100;
        private Semaphore mutex = new Semaphore(1);
        private Semaphore notFull = new Semaphore(n);
        private Semaphore notEmpty = new Semaphore(0);

        public void product() {
            // 缓冲区满时，生产者线程必须等待
            notFull.P();
            mutex.P();

            mutex.V();
            // 唤醒等待的消费者线程
            notEmpty.V();
        }

        public void  consume() {
            // 缓冲区空时，消费线程等待
            notEmpty.P();
            mutex.P();

            mutex.V();
            notFull.V();
        }
    }

    /**
     * Monitor 操作系统翻译为 管程。指的是管理共享变量以及对共享变量的操作过程，让它们支持并发
     * 管程在信号量的基础上，增加了条件同步，将上述复杂的操作封起来
     *
     *  入队和出队都必须获取互斥锁
     *
     *  wait 的正确姿势：
     *  while (条件不满足) {
     *      wait();
     *  }
     *
     *  MESA 管程里， T2 通知完还是会继续执行，T1 并不立即执行，仅仅从条件变量的等待队列进入入口等待队列里面。
     *  这样做的好处是 notify 不用放到代码的最后，T2 也没有多余的阻塞唤醒操作。但是也有个副作用，当 T1 再次执行的时候，
     *  可能满足的条件已经不满足了，所以需要循环方式检验条件变量
     *
     *  尽量使用 notifyAll，不要使用 notify
     *  使用 notify 的条件
     *      1. 所有等待线程拥有相同的等待条件
     *      2. 所有等待线程被唤醒后，执行相同的操作
     *      3. 只需要唤醒一个线程
     *
     *  AQS 原理
     *      内部包含两个队列，一个是同步队列，一个是等待队列
     *      同步队列：锁被占用时，会将该线程添加到同步队列中。当锁释放时，会从队列中唤醒一个线程
     *      等待队列：当调用 await 时，会将线程添加到等待队列中。当其他线程调用 notify，会将该线程从等待队列移动到同步队列中，重新竞争锁
     */
    private static class Monitor {
        final Lock lock = new ReentrantLock();
        // 条件变量：队列不满
        final Condition notFull = lock.newCondition();
        // 条件变量：队列不空
        final Condition notEmpty = lock.newCondition();

        @SuppressWarnings("Duplicates")
        void end(Object o) {
            lock.lock();
            try {
                boolean full = true;
                while (full) {
                    try {
                        notFull.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 入队后，通知可以出队
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }

        @SuppressWarnings("Duplicates")
        void deq() {
            lock.lock();
            try {
                boolean empty = true;
                while (empty) {
                    // 等待队列不空
                    try {
                        notEmpty.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 出队后，通知可入队
                notFull.signal();
            } finally {
                lock.unlock();
            }
        }

    }
}