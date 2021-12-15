package com.microwu.cxd.network.netty.ds.thread;

import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Netty 线程池的设计思想
 *
 * // 默认是 2 * CPU
 * DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt(
 *                 "io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));
 *
 * protected MultithreadEventLoopGroup(int nEventLoops, Executor executor, Object... args) {
 *          // 如果没有设置 nEventLoop，默认为 DEFAULT_EVENT_LOOP_THREADS
 *         super(nEventLoops == 0 ? DEFAULT_EVENT_LOOP_THREADS : nEventLoops, executor, args);
 *     }
 *
 * 构造器主要做了三件事：
 *  1. 创建 NioEventLoopGroup 的线程执行器 - ThreadPerTaskExecutor，它是对 Java 的线程池接口的实现，负责创建 NioEventLoopGroup
 *  底层 Java 线程 Thread
 *
 *  2. for 循环 + newChild(executor, args) 方法，去构建 nThreads 个 NioEventLoop 对象
 *
 *  for 循环前面的属性 children，它的类型是一个数组，即 EventExecutor，由此可知 EventLoopGroup 实现的线程池其实就是内部维护的类型为
 *  EventExecutor[] 的数组，其大小是 nThreads，而 EventExecutor 本身是一个 Netty 线程相关的接口，这样就构成了一个 Netty 线程池。
 *
 *  在 newChild 构建线程时，同时会将刚刚创建的线程执行器 - ThreadPerTaskExecutor 当做参数传入，去合力创建线程池线程，这个线程就是前面说的
 *  NioEventLoop 对象
 *
 *  3. 创建 NioEventLoopGroup 的线程选择器 - chooser
 *  chooser 的目的是可以给每个新连接 Channel 分配 NioEventLoop 线程
 *
 *  注意：newChild 方法中，会实例化 NioEventLoop 对象，实例化过程会顺便为该对象绑定一个队列，即所谓的异步任务队列，也叫多生产者单消费者队列
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247485225&idx=1&sn=da015df0cfa38557def9194e85c6fe08&chksm=fbc09129ccb7183fc4bb32add4537dc40b9564e0e7297b2159f18a655ad0568a39876d718746&scene=21#wechat_redirect
 *
 * 深入源码去理解Netty“线程”的设计思想
 *
 * newChild(executor, args); // 模板方法，实际调用的是子类 NioEventLoopGroup 的 newChild 方法
 *
 * SingleThreadEventExecutor 属性
 *  Executor executor; // executor 本质是一个线程，只不过它是 Netty 封装的线程执行器，隐藏了 JDK 的 Thread，Netty 用它来驱动异步任务
 *  Thread thread; // thread 是一个缓存本 NioEventLoop 组件绑定的那个线程对象
 *
 *  构造器：
 *      1. 保存了 NioEventLoop 的线程执行器，executor = ThreadPerTaskExecutor
 *      2. 创建一个异步任务队列 - taskQueue，多生产者单消费者
 *          |- 多生产者对应的是 Netty 外部线程，可能会有多个
 *          |- 单消费者是 Netty 的 NioEventLoop 线程，即当前这个 I/O 线程
 *      3. Netty 封装的每个 NIO 线程都会依赖一个 MpscQueue
 *
 *  NioEventLoop 构造器：
 *      |- 创建 Selector
 *      |- 创建 provider，优化用的，暂时不管
 *
 *
 * 总结：
 *  不要把 task 和 thread 混为一谈。线程可以驱动任务，Runnable 一种描述任务的方式，
 *  它不会产生任何线程的能力，要实现线程的行为，必须显示的将一个任务附着到 Thread 上，
 *  在驱动这个任务，才能让该任务实现线程的行为。 --- Java 编程思想
 *
 *  Executor 在客户端和任务执行之间提供了一个间接层，与客户端直接执行任务不同，这个中介
 *  将执行任务。Executor 允许管理异步任务的执行，而无须显示地管理线程的生命周期。
 *  Thread 类本身不执行任何操作，它只是驱动附着在它上面的任务，不要将线程和任务混为一谈。
 *                                      --- Java 编程思想
 *
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247485262&idx=1&sn=ec0a9870903c19e9e9c8617c4e5c99de&chksm=fbc0914eccb718589e2f2f08daa822df2858f496986d493a8f003d2c3e0615dd2cbe772a6b33&scene=178&cur_album_id=1338702322071076864#rd
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/16  20:29
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class Thread4 {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    }

}
