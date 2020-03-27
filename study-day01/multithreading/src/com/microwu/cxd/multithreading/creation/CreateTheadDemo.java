package com.microwu.cxd.multithreading.creation;

import java.util.concurrent.*;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/5   14:22
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CreateTheadDemo {
    public static void main(String[] args) {
        test03();

    }

    /**
     * 创建线程的三种方式
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/5  15:40
     *
     * @param
     * @return  void
     */
    public static void test() {
        // 1. 继承Thread
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread() + ":继承Thread");
                super.run();
            }
        };
        thread.start();
        // 2. 实现Runnable接口
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread() + ":实现Runnable接口");
            }
        }).start();
        // 3. 实现callable接口
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "实现Callable接口";
            }
        });
        try {
            String result = future.get();
            System.out.println(Thread.currentThread() + ":" + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    /**
     * 线程池异常处理: 没有抛出异常, 我们无法感知异常
     *  1. 添加try/catch
     *  2. 通过Future对象的get方法接受抛出的异常
     *  3. 为工作者线程设置UncaughtExceptionHandler, 在uncaughtException中处理异常
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/5  15:44
     *
     * @param
     * @return  void
     */
    public static void test02() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
//      try/catch 处理异常
        executorService.submit(() -> {
            try {
                System.out.println(Thread.currentThread());
                throw new RuntimeException("运行时异常");
            }catch (Exception e) {
                System.out.println("抛出异常");
            }
        });
        // 通过Future 处理异常
        Future<String> submit = executorService.submit(() -> {
            String s = null;
            s.toString();
            return s;
        });
        try {
            submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("抛出异常");
            e.printStackTrace();
        }
        executorService.shutdown();

    }

    public static void test03() {
        ExecutorService executorService = Executors.newFixedThreadPool(1, r -> {
            Thread thread = new Thread(r);
            thread.setUncaughtExceptionHandler(
                    (t1, e) -> {
                        System.out.println(t1 + "抛出的异常");
                    }
            );
            return thread;
        });
        // 必须使用execute, submit不能捕获异常
        executorService.execute(() -> {
           throw new RuntimeException("运行时异常...");
        });
        executorService.shutdown();
    }
}