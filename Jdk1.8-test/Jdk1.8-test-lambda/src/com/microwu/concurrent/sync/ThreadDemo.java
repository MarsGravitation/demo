package com.microwu.concurrent.sync;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/16   17:01
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ThreadDemo {

    /**
     * 创建线程方式1: 继承Thread
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/16  17:01
     *
     * @param
     * @return  void
     */
    public static void test() {
        new ThreadTest().start();
    }

    /**
     * 方式二: 实现 Runnable 接口, 作为参数传给Thread 对象
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/16  17:03
     *
     * @param
     * @return  void
     */
    public static void test02() {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
        }).start();
    }

    /**
     * 实现Call 接口
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/16  17:11
     *
     * @param
     * @return  void
     */
    public static void test03() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Executors.newScheduledThreadPool(1);
        Future<String> submit = executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName());
            return "success";
        });

        System.out.println(submit.get());

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test03();
    }

    /**
     * 实现run 方法
     */
    static class ThreadTest extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
        }
    }
}