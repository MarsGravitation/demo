package com.microwu.cxd.study.future;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * Future
 *
 * ListenableFuture 可以允许你注册回调方法（callbacks），在运算（多线程执行）
 * 完成的时候进行调用，或者在运算（多线程执行）完成后立即执行。
 *
 * ListenableFuture 中的基础方法是 addListener(Runnable, Executor)，
 * 该方法会在多线程运算完的时候，指定的 Runnable 参数传入的对象会被指定的 Executor
 * 执行。
 *
 * 添加回调 Callbacks
 *  Futures.addCallback(ListenableFuture<V>, FutureCallback<V>, Executor)
 *  addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback)
 *      默认采用 MoreExecutors.sameThreadExecutor() 线程池，为了简化使用，Callback 采用
 *      轻量级的设计
 *
 *  FutureCallback<V> 实现了两个方法：
 *      onSuccess(V)，在 Future 成功的时候执行
 *      onFailure(V)，在 Future 失败的时候执行
 *
 * ListenableFuture 的创建
 *  对应 JDK 中的 ExecutorService.submit(Callable) 提交多线程异步运算方式
 *  Guava 提供了 ListeningExecutorService 接口，该接口返回 ListenableFuture
 *  而相应的 ExecutorService 返回普通的 Future。将 ExecutorService 转换为 ListeningExecutorService，
 *  可以使用 MoreExecutors.listeningDecorator(ExecutorService) 进行装饰
 *
 *  假如你是从 FutureTask 转换而来的，Guava 提供 ListenableFutureTask.create(Callable<V>) 和
 *  ListenableFutureTask.create(Runnable<V>, V)，与 JDK 不同的是 ListenableFutureTask 不能
 *  被继承
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/7/5  15:52
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class FutureTest {

    /**
     * ListenableFuture 的创建
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/7/5     16:10
     *
     * @param
     * @return void
     */
    @SuppressWarnings("all")
    public static void test() {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<String> explosion = service.submit(new Callable<String>() {

            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread() + ": call");
                return "success";
            }
        });

        explosion.addListener(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread() + ": run");
            }
        }, MoreExecutors.directExecutor());

        Futures.addCallback(explosion, new FutureCallback<String>() {
            // we want this handler to run immediately after we push the big red button!
            public void onSuccess(String result) {
                System.out.println(Thread.currentThread() + ": success, result = " + result);
            }
            public void onFailure(Throwable thrown) {
                System.out.println(thrown); // escaped the explosion!
            }
        }, MoreExecutors.directExecutor());

    }

    @SuppressWarnings("all")
    public static void test02() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<?> future = executorService.submit(() -> {
            System.out.println(Thread.currentThread() + ": run");
            return "run";
        });

        ListenableFutureTask<String> listenableFutureTask = ListenableFutureTask.create(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread() + ": call");
                return "call";
            }
        });

        executorService.submit(listenableFutureTask);

        listenableFutureTask.addListener(() -> {
            System.out.println(Thread.currentThread() + ": listener");
        }, executorService);

    }

    public static void main(String[] args) {
        test();
//        test02();

    }

}
