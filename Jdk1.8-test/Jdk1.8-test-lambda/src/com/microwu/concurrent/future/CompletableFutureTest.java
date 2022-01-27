package com.microwu.concurrent.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1. 概述
 *  CompletableFuture 是一个可以在任务完成阶段触发一些操作 Future。简单的来讲，
 *  就是可以实现异步回调
 *
 * 2. 为什么引入 CompletableFuture
 *  Future 虽然提供了异步处理任务的能力，但是获取结果的方式很不优雅，还是需要通过
 *  阻塞（或轮训）的方式。如何避免阻塞呢？其实就是注册回调。
 *
 *  业界结合观察者模式实现异步回调。也就是当任务执行完成后去通知观察者。
 *
 * 3. 功能
 *  CompletableFuture 的功能主要体现在它的 CompletionStage
 *  - 转换 thenCompose
 *  - 组合 thenCombine
 *  - 消费 thenAccept
 *  - 运行 thenRun
 *  - 带返回的消费 thenApply
 *
 *  消费和运行的区别：
 *  消费使用执行结果。运行只是运行特定任务。
 *  CompletableFuture 借助 CompletionStage 的方法可以实现链式调用。并且可以选择同步或异
 *  步两种方式。
 *
 * https://blog.csdn.net/weixin_39332800/article/details/108185931?spm=1001.2014.3001.5502
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2022/1/24  10:27
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class CompletableFutureTest {

    public static void main(String[] args) {
        test();
    }

    /**
     * supplyAsync pool-1-thread-1
     * hello world
     * Thread[pool-1-thread-2,5,main]
     * zzzz
     * ssss
     * dddd
     *
     * 如果是同步执行 cf.thenRun。他的执行线程可能是 main 线程，也可能是执行源任务的线程。如果执行源
     * 任务的线程在 main 调用之前执行完了任务。那么 cf.thenRun 方法会由 main 线程调用。
     *
     * 如果同一个任务的依赖任务有多个：
     *  - 如果这些依赖任务都是同步执行。那么加入这些任务被当前调用线程 main 执行，则是有序执行，
     *  假如被执行源任务的线程执行，那么会是倒序执行。因为内部任务数据结构为 LIFO
     *  - 如果这些依赖任务都是异步执行，那么他会通过异步线程池去执行任务。不能保证任务的执行顺
     *  序。
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2022/1/24     10:54
     *
     * @param
     * @return void
     */
    @SuppressWarnings("all")
    public static void test() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("supplyAsync " + Thread.currentThread().getName());
            return "hello";
        }, executorService).thenApplyAsync(s -> {
            System.out.println(s + " world");
            return "hhh";
        }, executorService);

        cf.thenRunAsync(() -> {
            System.out.println("dddd");
        });
        cf.thenRun(() -> {
            System.out.println("ssss");
        });
        cf.thenRun(() -> {
            System.out.println(Thread.currentThread());
            System.out.println("zzzz");
        });
    }

}
