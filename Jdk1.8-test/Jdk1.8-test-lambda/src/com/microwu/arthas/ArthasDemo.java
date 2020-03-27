package com.microwu.arthas;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/15   15:43
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ArthasDemo {

    private static HashSet hashSet = new HashSet();

    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        // 模拟 CPU 过高
        cpu();
//        // 模拟线程阻塞
//        thread();
//        // 模拟线程死锁
//        deadThread();
//        // 不断向hashSet 集合增加数据
//        addHashSetThread();

    }

    public static void cpu() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cpuHigh();
//        cpuNormal();
    }

    /**
     * 极度消耗CPU的线程
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/15  15:49
     *
     * @param
     * @return  void
     */
    public static void cpuHigh() {
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("cpu start 100");
            }

        });

        // 添加到线程
        executorService.submit(thread);
    }

    /**
     * 普通消耗CPU的线程
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/15  15:49
     *
     * @param
     * @return  void
     */
    public static void cpuNormal() {
        for(int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {
                    System.out.println("cpu start");
                    try{
                        Thread.sleep(3000);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void thread() {
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("cpu start");
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // 添加到线程
        executorService.submit(thread);
    }

    private static void deadThread() {
        Object o1 = new Object();
        Object o2 = new Object();

        new Thread(() -> {
            synchronized (o1) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {

                }
            }
        }).start();

        new Thread(() -> {
            synchronized (o2) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {

                }
            }
        }).start();
    }

    private static void addHashSetThread() {
        // 初始化常量
        new Thread(() -> {
            int count = 0;
            while (true) {
                try {
                    hashSet.add("count" + count);
                    Thread.sleep(10000);
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}