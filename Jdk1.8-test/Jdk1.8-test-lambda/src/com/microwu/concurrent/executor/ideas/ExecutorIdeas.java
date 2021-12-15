package com.microwu.concurrent.executor;

/**
 *
 * 有一段代码需要异步执行？ new Thread(r).start();
 *
 * 封装成一个工具类？
 *
 * public interface Executor {
 *     public void execute(Runnable r);
 * }
 *
 * 第一版：
 *  class FlushExecutor implements Executor {
 *      public void execute(Runnable r) {
 *          new Thread(r).start();
 *      }
 *  }
 *
 *  缺点：10000 个人调用这个工具类提交任务，就会创建 10000 个线程
 *
 * 第二版：
 *  把任务 r 丢到一个 tasks 队列中，然后只启动一个线程，Worker 线程 不断从 tasks 队列中取任务，
 *  执行任务。这样无论调用者调用多少次，永远只有一个 Worker 线程在运行
 *
 *  优点：
 *      1. 控制了线程数量
 *      2. 队列不但起到了缓冲的作用，还将任务的提交与执行解耦了
 *      3. 解决了每次重复创建和销毁线程带来的系统开销
 *
 *  缺点：一个后台的工作线程 Worker 有点少？tasks 队列满了怎么办？
 *
 *
 * 第三版：
 *  1. 初始化线程池时，直接启动 corePoolSize 个工作线程 Worker 线程先跑着
 *  2. 这些 Worker 就是死循环从队列里取任务然后执行
 *  3. execute 方法仍然时直接把任务放到队列，但是队列满了之后直接抛弃
 *
 *
 * 第四版：
 *  1. 按需创建 Worker，刚初始化线程池时，不再立刻创建 corePoolSize 个工作线程，
 *  而是等待调用者不断提交任务的过程中，逐渐把工作线程 Worker 创建出来，等数量达到了
 *  corePoolSize 时就停止，把任务直接丢到队列里。
 *  2. 添加拒绝策略，增加一个入参，类型是一个接口 RejectedExecutionHandler，由
 *  调用者决定实现类，以便在任务提交失败后执行 rejectedExecution
 *  3. 增加线程工厂：实际上就是增加一个入参，类型是一个接口 ThreadFactory，增加工作
 *  线程时不再直接 new 线程，而是调用这个由调用者传入的 ThreadFactory 实现类的 newThread
 *  方法
 *
 * 第五版：增加弹性
 * 增加新属性，最大线程数 maximumPoolSize。当核心线程数和队列都满了，新提交的任务仍然可以通过
 * 创建新的工作线程（非核心线程），直到工作线程数达到了 maximumPoolSize 为止，这样就可以缓解
 * 一时的高峰期
 *  1. 当 workCount < corePoolSize，创建新的 Worker 来执行任务
 *  2. 当 workCount >= corePoolSize 就把任务直接丢到队列里
 *  3. 队列已满且 workCount < maximumPoolSize 时，创建非核心线程，知道 workCount =
 *  maximumPoolSize，再走拒绝策略
 *
 *  非核心线程设置一个超时时间 keepAliveTime，如果这么长时间没能从队列里获取到任务，就销毁线程
 *
 *
 * https://www.cnblogs.com/flashsun/p/14368520.html
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/23  19:43
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class ExecutorIdeas {
}
