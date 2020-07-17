package com.microwu.concurrent.executor;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description: 线程池源码分析
 *  1. 继承结构
 *        Executor - 顶级接口，定义 execute(Runnable runnable)
 *              |
 *     ExecutorService - 接口，在Executor 基础上添加了很多接口方法，一般我们会使用这个接口
 *             |
 *      AbstractExecutorService - 抽象类，实现了非常有用的一下方法供子类直接使用
 *              |
 *       ThreadPoolExecutor - 这个类提供了关于线程池所需的非常丰富的功能
 *
 *  Executors 类：工具类，用于生成 ThreadPoolExecutor 实例的方法
 *  另外，由于线程池支持 获取线程执行的结果，所以，引入了 Future 接口，RunnableFuture 继承自此接口，我们最需要关心的就是它的实现类 FutureTask
 *
 *  在线程池的使用过程中，我们是往线程池提交任务（Task），使用过线程池的都知道，我们提交的每个任务是实现了 Runnable 接口的，其实就是先将 Runnable
 *  的任务包装成 FutureTask，然后再提交到线程池。FutureTask，首先是一个任务（Task），然后具有Future 接口的语义，即可以在将来（Future）得到执行结果
 *
 *  Executor 接口：void execute(Runnable command)，代表提交一个任务
 *  ExecutorService 接口：
 *      void shutdown() - 关闭线程池，以提交的任务继续执行，不接受继续提交的任务
 *      void shutdownNow() - 关闭线程池，尝试停止正在执行的所有任务，不接受继续提交新任务
 *      boolean isShutdown() - 线程池是否以关闭
 *      boolean isTerminated() - 如果调用了 shutdown() 或 shutdownNow(), 所有任务结束了，那么返回 true
 *                              这个方法必须调用 shutdown 或者 shutdownNow 才会返回 true
 *      boolean awaitTermination - 等待所有任务完成，并设置超时时间
 *      submit - 提交一个 Callable 任务
 *
 *  FutureTask
 *      Future              Runnable
 *          \                   /
 *           \                 /
 *              RunnableFuture
 *                      |
 *                      |
 *                  FutureTask
 *  FutureTask 通过 RunnableFuture 间接实现了 Runnable 接口，
 *  所以每个 Runnable 通常包装成 FutureTask，然后调用 executor.execute(Runnable command) 交给线程池
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/4   10:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ThreadPoolExecutorDemo {

    public static void test() {
        // 我们经常这样创建一个线程
        new Thread(() -> {
            // do something
        }).start();

        // 用线程池
        Executor executor = Executors.newFixedThreadPool(1);
        executor.execute(() -> {
            // do something
        });
    }

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 1, null, null);
        /*
            1. 如果当前线程少于核心线程数，直接添加一个 worker 来执行任务
            2. 如果当前线程数大于核心线程数或者添加 worker 失败，把任务添加到 workQueue中
            3. 如果 workQueue 队列满了，以 max 为界创建新的 worker，如果失败，执行拒绝策略
         */
        threadPoolExecutor.execute(null);
    }

    /**
     * 演示线程池同步执行每个任务
     */
    class DirectExecutor implements Executor {

        @Override
        public void execute(Runnable command) {
            // 这里不是用的 new Thread(r).start()，也就是没有启动任何一个新线程
            command.run();
        }
    }

    /**
     * 每个任务提交进来，直接启动一个新的线程来执行任务
     */
    class ThreadPerTaskExecutor implements Executor {

        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    }

    /**
     * 组合两个Executor 使用
     * 实现：将所有任务都添加到一个 queue 中，然后从 queue 中取任务，交给真正的执行器执行
     */
    class SerialExecutor implements Executor {
        // 任务队列
        final Queue<Runnable> tasks = new ArrayDeque<>();
        // 真正的执行器
        final Executor executor;
        // 当前正在执行的任务
        Runnable active;

        // 初始化的时候，执行执行器
        SerialExecutor(Executor executor) {
            this.executor = executor;
        }

        /**
         * 添加任务到线程池：将任务添加到任务队列，scheduleNext 触发执行器去任务队列取任务
         *
         * 举例：将三个任务交给线程池执行，第一个进去，放入队列，active == null，从队列中取出来执行，
         *      当它执行完会调用下一个任务执行
         *
         * @author   chengxudong               chengxudong@microwu.com
         * @date    2020/6/4  14:30
         *
         * @param   	command
         * @return  void
         */
        @Override
        public synchronized void execute(final Runnable command) {
            // 加入队尾
            tasks.offer(() -> {
                // 将任务进行包装，最后调用队列中的下一个任务执行
                try {
                    command.run();
                } finally {
                    scheduleNext();
                }
            });
            if (active == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            // 从队头取
            if ((active = tasks.poll()) != null) {
                // 具体的执行转给真正的执行器 Executor
                executor.execute(active);
            }
        }
    }

}