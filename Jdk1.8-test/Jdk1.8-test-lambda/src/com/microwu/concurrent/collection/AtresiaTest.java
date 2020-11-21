package com.microwu.concurrent.collection;

import java.util.concurrent.*;

/**
 * Description: 闭锁
 *  闭锁是一种同步工具类，可以延迟线程的进度直到其达到终止状态。闭锁的作用相当于一扇门：在闭锁到达结束状态之前，这扇门一直是关闭的，
 *  并且没有任何线程能通过，当达到结束状态时，这扇门会打开并允许所有的线程通过。当闭锁达到结束状态后，将不会再改变状态，因此这扇门将
 *  永远保持打开状态。闭锁可以用来确保某些活动知道其他活动都完成后才继续执行
 *
 *
 *  countDownLatch 是一种灵活的闭锁实现。
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/15   16:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AtresiaTest {

    /**
     * CountDownLatch：如果非 0 阻塞
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/15  16:58
     *
     * @param
     * @return  void
     */
    private static void test() {

    }

    /**
     * FutureTask 也可以实现闭锁
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/15  17:00
     *
     * @param
     * @return  void
     */
    private static void test02() {

    }

    /**
     * 信号量：用来控制同时访问的某个特定资源的操作数量，或者同时执行某个指定操作的数量，还可以用来实现某种资源池，或者对容器施加边界
     * Semaphore 管理着一组虚拟许可 permit。在执行操作时后可以首先获得许可，使用后释放许可。如果没有许可 acquire 阻塞。release 返回一个许可
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/15  17:00
     *
     * @param
     * @return  void
     */
    private static void test03() {
        ExecutorService pool = Executors.newCachedThreadPool();
        // 只能 5 个线程访问
        final Semaphore semaphore = new Semaphore(5);
        for (int i = 0; i < 20; i++) {
            pool.execute(() -> {
                try {
                    // 获取许可
                    semaphore.acquire();
                    // 访问后，释放
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
    }

    /**
     * 栅栏类似于闭锁，它能阻塞一组线程知道某个事件发生。闭锁是一次性对象，一旦进入最终状态，就不能被重置了。
     *
     * 栅栏与闭锁的关键区别在于，所有线程必须同时达到栅栏位置，才能继续执行。闭锁用于等待事件，而栅栏用于等待其他线程
     *
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/15  17:10
     *
     * @param
     * @return  void
     */
    public static void test04() {
        int n = 4;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(n);
        for (int i = 0; i < n; i++) {
            new Writer(cyclicBarrier).start();
        }

        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("CyclicBarrier 重用。。。");
        for (int i = 0; i < n; i++) {
            new Writer(cyclicBarrier).start();
        }
    }

    private static class Writer extends Thread {
        private CyclicBarrier cyclicBarrier;

        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程 " + Thread.currentThread().getName() + " 正在写入数据 。。。");
            try {
                Thread.sleep(1000);
                System.out.println("线程 " + Thread.currentThread().getName() + " 写入数据完毕，等待其他线程写入完毕 。。。");
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 所有线程写入完毕，继续处理其他任务。。。");
        }
    }

    /**
     * 交换机
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/15  17:29
     *
     * @param
     * @return  void
     */
    public static void test05() {

    }

    /**
     * 阻塞队列
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/15  17:29
     *
     * @param
     * @return  void
     */
    public static void test06() {

    }

    public static void main(String[] args) {
        test04();
    }
}