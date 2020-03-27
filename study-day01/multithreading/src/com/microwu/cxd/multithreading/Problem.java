package com.microwu.cxd.multithreading;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:     多线程安全问题
 *                  最终的结果一定是小于或等于200的, 因为这段代码是非线程安全的
 *                  加了同步锁之后, count自增的操作变成了原子性操作, 所以最终输出一定是200, 代码实现了线程安全
 *                  虽然synchronized确保了线程安全, 但是在某些情况下, 这并不是一个最优的选择
 *
 *                  synchronized关键字会让没有得到锁资源的线程进入BLOCKED状态, 而后在争夺到锁资源后恢复为RUNNABLE状态,
 *                  这个过程涉及到操作系统用户模式和内核模式的装换, 代价比较高
 *                  尽管Java1.6位synchronized做了优化, 增加了从偏向锁向轻量级锁再到重量级锁的锁过度,但是在最终转变为
 *                  重量级锁之后, 性能仍然比较低.
 *                  所谓原子操作类, 指的是Atomic开头的包装类, 用于Boolean, Integer类型的原子性操作
 *
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/16   9:26
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Problem {
//    private static Integer count = 0;
    public static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) {
        // 开启两个线程
        for(int i = 0; i < 2; i++) {
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
//                            try {
//                                Thread.sleep(10);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            // 每个线程count自增100
                            for(int i = 0; i < 100; i++) {
//                                synchronized (Problem.class) {
//                                    count++;
//                                }
                                count.incrementAndGet();
                            }
                            System.out.println(Thread.currentThread().getName() + ":" + count.get());
                        }
                    }
            ).start();
        }
    }
}