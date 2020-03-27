package com.microwu.concurrent.sync;

/**
 * Description: 停止线程
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/21   9:53
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class StopThread {

    /**
     * interrupt 并不会马上停止线程, 仅仅是设置一个打断的标志
     * -- Just to set the interrupt flag
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/2/21  9:58
     */
    private static void test() {
        Thread thread = new Thread(() -> {
            int i = 0;
            while (true) {
                System.out.println(i++);
            }
        });

        thread.start();
        thread.interrupt();
    }

    private static void test02() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
//                System.out.println("hello world ~~~");
            }
        });

        thread.start();

        Thread.sleep(1000);

        // 测试线程是否中断, 而且会重置中断状态

        // 结果 false false
//        thread.interrupt();
//        System.out.println(Thread.interrupted());
//        System.out.println(Thread.interrupted());

        // 结果 true, true
        // 判断标志位
        System.out.println(thread.isInterrupted());
        System.out.println(thread.isInterrupted());

        // 结果 true, false

        // 当前线程被中断, 返回true, 并且重置标志为 false
        // 第二次返回 false
        Thread.currentThread().interrupt();
        System.out.println(Thread.interrupted());
        System.out.println(Thread.interrupted());

    }

    /**
     * 线程被终止, 但是 for 循环 之后还会执行
     * 我认为 并没有真正中断, 只是将标志位设置为 true
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/21  10:31
     *
     * @param
     * @return  void
     */
    private static void test03() throws InterruptedException {
        Thread thread = new Thread(() -> {
            int i = 0;
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(i++);
            }

            System.out.println("线程被中断 ~~~");
        });

        thread.start();

        Thread.sleep(500);

        thread.interrupt();

    }

    /**
     * 通过异常 中断线程
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/21  10:35
     *
     * @param
     * @return  void
     */
    private static void test04() {
        Thread thread = new Thread(() -> {
            try {
                int i = 0;
                while(i++ < 50000) {
                    if(Thread.currentThread().isInterrupted()) {
                        throw new RuntimeException("中断线程 ~~~");
                    }
                }

                System.out.println(" 中断之后执行的代码 ~~~");
            }catch (Exception e) {
                System.out.println("被中断 ~~~");
            }
        });

        thread.start();

        thread.interrupt();

    }

    /**
     * sleep 中断线程
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/21  10:44
     *
     * @param   	
     * @return  void
     */
    private static void test05() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // 中断异常, 并且清除 中断标识
                System.out.println(Thread.interrupted());
                e.printStackTrace();
            }
        });
        thread.start();
        thread.interrupt();

    }

    public static void main(String[] args) throws InterruptedException {
        test05();
    }

}