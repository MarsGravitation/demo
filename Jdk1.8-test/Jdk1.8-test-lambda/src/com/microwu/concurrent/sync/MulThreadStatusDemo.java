package com.microwu.concurrent.sync;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/13   16:51
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MulThreadStatusDemo {

    private static int a = 0;

    /**
     *  结果:
     *      java.lang.InterruptedException: sleep interrupted
     *      Thread-0 : false, TERMINATED
     *      Thread-1 : true, RUNNABLE
     *
     *  描述:
     *      t1.interrupted() - 打断t1 线程
     *      此方法不一定打断线程, 仅是将标志位置为true, 由线程自己判断
     *
     *      t2 不会被中断
     *
     *      t1线程抛出 interruptedException, 当线程sleep, wait, join,
     *      此时打断线程会抛出异常
     *
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/13  16:51
     *
     * @param
     * @return  void
     */
    private static void test01() throws InterruptedException {
        // 线程1 睡眠 1秒
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 线程2 死循环
        Thread thread2 = new Thread(() -> {
            while(true) {

            }
        });

        thread1.start();
        thread2.start();

        thread1.interrupt();
        thread2.interrupt();

        while(thread1.isInterrupted()){

        }

        Thread.sleep(10000);

        System.out.println(thread1.getName() + " : " + thread1.isInterrupted() + ", " + thread1.getState());
        System.out.println(thread2.getName() + " : " + thread2.isInterrupted() + ", " + thread2.getState());

    }

    /**
     * 现象: 如果不加join, 结果为0
     * 原因: 主线程main方法执行System.out.println()时, 另一个线程还还没有真正开始运行
     *      或许正在为它分配资源准备运行. 因为为线程分配资源需要时间, 而main方法执行完
     *      t.start 方法后继续向下执行, 得到的值为0
     *
     * join方法: 当一个线程执行完, 另一个线程才继续执行
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/13  17:29
     *
     * @param
     * @return  void
     */
    private static void test02() {
        JoinThread joinThread = new JoinThread();
        joinThread.start();
        try {
            joinThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(a);

    }

    public static void main(String[] args) throws InterruptedException {
        test01();
    }

    static class JoinThread extends Thread {

        @Override
        public void run() {
            for(int i = 0; i < 10; i++) {
                a++;
            }
            System.out.println(Thread.currentThread());
        }
    }
}