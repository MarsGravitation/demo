package com.microwu.concurrent.executor;

/**
 * Description: Java线程池实现原理及其在美团业务中的实践
 *  1. 写在前面
 *  2. 线程池核心设计与实现
 *      2.1 总体设计
 *      Executor -> ExecutorService -> AbstractExecutorService -> ThreadPoolExecutor
 *      ThreadPoolExecutor 实现的顶层接口是 Executor，顶层接口 Executor 提供了一种思想：将任务提交和任务执行进行解耦。用户无需关注如何创建线程，如何调度线程来执行任务，用户只需提供
 *      Runnable 对象，将任务的运行逻辑提交到执行器 Executor 中，由 Executor 框架完成线程的调配和任务的执行部分。
 *
 *      ExecutorService 接口增加了一些能力：扩充执行任务的能力，补充可以为一个或一批异步任务生成 Future 的方法；提供了管控线程池的方法，比如停止线程池的运行
 *
 *      AbstractExecutorService 是上层的抽象，将执行任务的流程串联起来，保证下层的实现只需要关注一个执行任务的方法即可。
 *      ThreadPoolExecutor 实现最复杂的运行部分，ThreadPoolExecutor 一方面维护自身的声明周期，另一方面管理线程和任务，是两者良好的结合从而执行并行任务
 *
 *      线程池在内部实际上构建了一个生产者消费者模型，将线程和任务两者解耦，并不直接关联，从而良好的缓冲任务，复用线程。线程池的运行主要分成两个部分：任务管理和线程管理。任务管理部分充当
 *      生产者角色，当任务提交后，线程池会会判断该任务后续的流转：直接申请线程执行该任务；缓冲到队列中等待线程执行；拒绝该任务。线程管理部分是消费者，它们被统一维护在线程池内，根据任务请求
 *      进行线程分配，当线程执行完任务后则会继续获取新的任务去执行，最终当线程获取不到任务时线程就会被回收
 *
 *      2.2 生命周期管理
 *      线程池内部使用一个变量维护维护两个值：运行状态 runState 和有效线程的数量 workerCount，高 3 位存 runState，低 29 位存 workerCount。用一个变量保存减少锁，使用位运算加快速度等。
 *
 *      2.3 任务执行机制
 *      2.3.1 任务调度
 *          a. 检测线程池状态，不是 Running 直接拒绝
 *          b. workerCount < corePoolSize，创建并启动一个线程来执行新提交的任务
 *          c. workerCount >= corePoolSize，且线程池内的阻塞队列未满，将任务添加到该阻塞队列中
 *          d. workerCount >= corePoolSize && workerCount < maximumPoolSize，且线程池内的阻塞队列已满，则创建并启动一个线程池来执行新提交的任务
 *          e. workerCount >= maximumPoolSize，并且阻塞队列已满，根据拒绝策略来处理该任务
 *
 *      2.3.2 任务缓冲
 *      线程池中是以生产者消费者模式，通过一个阻塞队列来实现的。阻塞队列缓存任务，工作线程从阻塞队列中获取任务
 *
 *      2.3.3 任务申请
 *      任务直接由新创建的线程执行；线程从任务队列中获取任务然后执行，执行完任务的空闲线程再次从队列中申请任务再去执行
 *      getTask 为了控制线程的数量，如果线程池现在不应该持有那么多线程，就会返回 null。工作线程 Worker 会不断接受新任务去执行，而当工作线程 Worker 接受不到任务时，开始被回收
 *
 *      2.3.4 任务拒绝
 *
 *      2.4 Worker 线程管理
 *      2.4.1 Worker 线程
 *      Worker 实现了 Runnable 接口，并持有一个线程 Thread，一个初始化任务 firstTask。thread 是通过 ThreadFactory 创建的；firstTask 用它来保存第一个任务。如果非空，线程启动就会执行这个任务，
 *      对应核心线程创建的情况；如果为 null，创建一个线程去执行任务列表 workQueue 中的任务，也就是非核心线程的创建
 *      线程池使用一张 Hash 表去持有线程的引用，可以通过添加、移除来控制线程的生命周期。
 *      Worker 是通过继承 AQS 实现独占锁。lock 一旦获取独占锁，表示线程正在执行任务，不应该中断线程；不是独占锁状态，也就是空闲，可以进行中断。
 *
 *      2.4.2 Worker 线程增加
 *
 *      2.4.3 Worker 线程回收
 *      依赖 JVM 自动回收，线程池的工作是维护一定数量的线程引用，防止这部分线程被 JVM 回收，当线程池回收线程时，只需要将其引用移除即可。Worker 被创建出来嘛不断轮训，获取任务，核心线程可以无线等待，
 *      非核心线程要限时获取任务。当 Worker 无法获取任务，也就是获取的任务为空时，Worker 会主动消除自身在线程池内的引用
 *
 *      2.4.4 Worker 执行任务
 *          a. while 循环不断 getTask
 *          b. 从阻塞队列中取任务
 *          c. 如果线程池正在停止，保证当前线程是中断状态
 *          d. 执行任务
 *          5. getTask == null，跳出循环，销毁线程
 *
 *  3. 线程池在业务中的实践
 *      参数动态化
 *      线程池监控：负载 = activeCount/maximumPoolSize
 *
 *
 * https://mp.weixin.qq.com/s?__biz=MjM5NjQ5MTI5OA==&mid=2651751537&idx=1&sn=c50a434302cc06797828782970da190e&chksm=bd125d3c8a65d42aaf58999c89b6a4749f092441335f3c96067d2d361b9af69ad4ff1b73504c&scene=21#wechat_redirect
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/30   9:42
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ExecutorDemo02 {
}