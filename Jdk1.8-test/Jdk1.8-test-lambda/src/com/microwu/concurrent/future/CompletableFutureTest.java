package com.microwu.concurrent.future;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 0x00. 前言
 *  Java 提供 Runnable Future<V> 两个接口用来实现异步任务逻辑。
 *  虽然 Future<V> 可以获取任务执行结果，但是获取方式十分不便。我们不得不使用
 *  Future#get 阻塞调用线程，或者使用轮训方式判断 Future#isDone 任务是否
 *  结束，再获取结果。
 *
 *  JDK8 之前并发类库没有提供相关的异步回调实现方式。我们只好借助第三方类库，
 *  如 Guava，扩展 Future，增加支持回调功能。
 *
 *  但是还是无法解决多个异步任务需要相互依赖的场景。
 *
 *  任务一和任务二可以单独执行。但是任务三必须等待任务一与任务二结束才能执行
 *  这里可以借助 CountDownLatch
 *
 * 0x01. CompletableFuture
 *  对比 Future, CompletableFuture 优点在于：
 *      - 不需要手工分配线程，JDK 自动分配
 *      - 代码语义清晰，异步任务链式调用
 *      - 支持编排异步任务
 *
 *  static CompletableFuture<U> completedFuture(U value)
 *  // 使用 forkjoin 公共线程池
 *  // 使用自定义线程池
 *
 *  第一个方法创建具有默认结果的 CompletableFuture
 *
 *  前两个方法 runAsync 不支持返回值，而 supplyAsync 支持返回结果
 *
 *  使用共享线程池将会有个弊端，一旦有任务被阻塞，将会造成其他任务没机会执行。
 *  所以强烈建议使用后两个方法，根据任务类型不同，主动创建线程池，进行资源隔离，
 *  避免互相干扰。
 *
 * 1.3 设置任务结果
 *  boolean complete(t value)
 *  boolean completeExceptionally(Throwable ex)
 *
 * 1.4 CompletionStage
 *  CompletableFuture implement Future, CompletionStage
 *
 *  任务时序关系分为几种：
 *      - 串行执行关系
 *      - 并行执行关系
 *      - AND 汇聚关系
 *      - OR 汇聚关系
 * 1.5 串行执行关系
 *  下一个任务必须等待上一个任务完成才可以继续执行
 *
 *  thenApply: 这个类核心方法为 R apply(T t)，所以这个接口会把上一个结果
 *  返回结果当做入参，执行结束将会返回结果。
 *
 *  thenAccept 方法需要传入参数对象为 Consumer<T> 类型，这个类核心方法为：
 *  void accept(T t) 不支持返回结果，但是需要将上一个任务执行结果当做参数传入
 *
 *  thenRun 不支持传入参数，也不会返回执行结果
 *
 * 1.6 AND 汇聚关系
 *  所有任务完成之后，才能进行下一个任务
 *
 *  thenCombine 方法核心参数 BiFunction，作用与 Function 一样，只不过
 *  BiFunction 可以接收两个参数，而 Function 只能接受一个参数
 *
 *  thenAcceptBoth 方法核心参数 BiConsumer 作用也与 Consumer 一样，不过
 *  其需要接收两个参数
 *
 *  runAfterBoth 方法核心参数最简单
 *
 *  以上只能完成两个任务 AND 汇聚关系，如果需要完成多个任务汇聚关系，需要使用
 *  CompletableFuture#allOf，这个结果不支持返回结果
 *
 * 1.7 OR 汇聚关系
 *  只要多个任务中任一任务完成就可以接着执行下一任务
 *
 *  传参与 AND 汇聚关系一致
 *
 * 1.8 异常处理
 *  CompletableFuture 方法执行过程若产生异常，当调用 get，join 获取
 *  任务结果才会抛出异常。
 *
 *  exceptionally 类似于 try...catch 中 catch 代码块中异常处理
 *  whenComplete 与 handle 类似于 finally 代码块
 *
 * https://zhuanlan.zhihu.com/p/111841508
 *
 * 计算结果完成时的处理
 *  whenComplete
 *  whenCompleteAsync
 *  whenCompleteAsync
 *  exceptionally
 *
 *  Action 的类型是 BiConsumer, Throwable, 它可以处理正常的计算结果，或异常情况
 *  不以 Async 结尾，意味着 Action 使用相同的线程执行
 *  Async 可能使用其他的线程去执行
 *
 *  注意这几个方法都会返回 CompletableFuture，当 Action 执行完毕后它的结果返回
 *  原始的 CompletableFuture 的计算结果或返回异常
 *
 *  转换
 *  CompletableFuture 可以作为 monad（单子）和 functor。由于回调风格
 *  的实现，我们不必因为等待一个计算完成而阻塞着调用线程，而是告诉 CompletableFuture
 *  当计算完成的时候请执行某个 Function。而且我们还可以将这些操作串联起来，
 *  或者将 CompletableFuture 组合起来。
 *
 *  thenApplyXxx
 *  这一组函数的功能是当原来的 CompletableFuture 计算完成后，将结果传递给
 *  函数 fn，将 fn 的结果作为新的 CompletableFuture 计算结果。
 *
 *  纯消费（执行 Action）
 *  thenAcceptXxx
 *  只有输入，没有输出
 *
 *  Runnable 类型的参数会忽略计算的结果，Consumer 是纯消费计算结果，
 *  BiConsumer 会计算另外一个 CompletionStage 纯消费，Function
 *  会对计算结果做转换，BiFunction 会组合另外一个 CompletionStage
 *  的计算结果做转换
 *
 *  组合
 *  thenComposeXxx
 *
 *  Either
 *  xxxEither
 *  当任意一个 CompletionStage 完成的时候，action 这个消费者就回被执行
 *
 *  辅助方法 allOf 和 anyOf
 *
 *  allOf 是所有 CompletableFuture 都执行完后执行计算
 *  anyOf 方法是当任意一个 CompletableFuture 执行完后就会执行计算
 *
 * https://colobu.com/2016/02/29/Java-CompletableFuture/
 *
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

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        test();
        test03();
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
            System.out.println(s + " world " + Thread.currentThread().getName());
            return "hhh";
        }, executorService);

        cf.thenRunAsync(() -> {
            System.out.println("dddd " + Thread.currentThread().getName());
        });
        cf.thenRun(() -> {
            System.out.println("ssss " + Thread.currentThread().getName());
        });
        cf.thenRun(() -> {
//            System.out.println(Thread.currentThread());
            System.out.println("zzzz " + Thread.currentThread().getName());
        });
    }

    public void test02() {
        // thenApply
        // 串行执行
        CompletableFuture.supplyAsync(() -> "hello, world")
                .thenApply(s -> s + "@cxd")
                .thenApply(String::toUpperCase);
        // AND 汇聚关系

        // 任务一 订购航班
        CompletableFuture<String> orderAirplane = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询航班");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("订购航班");
            return "航班信息";
        });
        // 任务二 订购酒店
        CompletableFuture<String> orderHotel = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询酒店");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("订购酒店");
            return "酒店信息";
        });
        // 任务三 任务一与任务二都完成，才能去订购租车服务
        CompletableFuture<String> hireCar = orderHotel.thenCombine(orderAirplane, (airplane, hotel) -> {
            System.out.println("根据航班加酒店订购租车服务");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "租车信息";
        });
        // 等待任务
        System.out.println(hireCar.join());

        // OR 关系
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            return "hello, world";
        });

        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            return "cxd";
        });

        CompletableFuture<String> cf3 = cf2.applyToEither(cf, s -> s);
        System.out.println(cf3.join());
    }

    private static Random random = new Random();
    private static long t = System.currentTimeMillis();

    static int getMoreData() {
        System.out.println("begin to start compute");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("end to start compute. passed " + (System.currentTimeMillis() - t) / 1000 + " seconds");
        return random.nextInt(1000);
    }

    /**
     * begin to start compute
     * end to start compute. passed 1 seconds
     * 222
     * null
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2023/3/31 15:19
     *
     * @param:
     * @return: void
     */
    public static void test03() throws ExecutionException, InterruptedException {
        // 创建 CompletableFuture 对象
//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> (".00");

        // 计算结果完成时的处理
        // 方法不以 Async 结尾，意味着 Action 使用相同的线程执行
        // 而 Async 可能会使用其他的线程去执行
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(CompletableFutureTest::getMoreData);
//        future.whenComplete((v, e) -> {
//            System.out.println(v);
//            System.out.println(e);
//        });
//        try {
//            future.get();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }

        // 转换
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 100);
        CompletableFuture<String> future1 = future.thenApplyAsync(i -> i * 10).thenApply(Object::toString);
        System.out.println(future1.get());

        // 纯消费
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> 100);
        CompletableFuture<Void> future3 = future2.thenAccept(System.out::println);
        System.out.println(future3.get());

        // 组合
        CompletableFuture<Integer> future4 = CompletableFuture.supplyAsync(() -> 100);
        CompletableFuture<String> future5 =  future.thenCompose( i -> {
            return CompletableFuture.supplyAsync(() -> {
                return (i * 10) + "";
            });
        });
        System.out.println(future5.get());

        // and
        CompletableFuture<Integer> future6 = CompletableFuture.supplyAsync(() -> 100);
        CompletableFuture<Integer> future7 = CompletableFuture.supplyAsync(() -> 200);
        CompletableFuture<Integer> future8 = future6.thenCombine(future7, Integer::sum);
        System.out.println(future8.get());

        // either
    }

}
