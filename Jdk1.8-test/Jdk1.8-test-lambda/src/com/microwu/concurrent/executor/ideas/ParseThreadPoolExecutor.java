package com.microwu.concurrent.executor.ideas;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 深度解读 Java 线程池设计思想及源码实现
 *
 * interface Executor: execute(Runnable runnable);
 *
 * interface ExecutorService: 在 Executor 接口的基础上添加了很多接口的方法，所以一般来说我们会使用这个接口
 *
 * AbstractExecutorService: 抽象类，实现了一些方法供子类直接使用
 *
 * ThreadPoolExecutor: 线程池
 *
 * Executors: 工具类
 *
 * 另外，由于线程池支持获取线程执行的结果，所以，引入了 Future 接口，RunnableFuture 继承自此接口，它的实现类 FutureTask。
 * 在线程池的使用过程中，我们是往线程池提交任务 - task，我们提交的每个任务是实现了 Runnable 接口，其实就是先将 Runnable 的
 * 任务包装成了 FutureTask，然后再提交到线程池。
 *
 * FutureTask: 它首先是一个任务 - task，然后具有 Future 接口的语义，即可以在将来 Future 得到执行结果
 *
 * Executor:
 *  void execute(Runnable command);
 *
 *  代表提交一个任务
 *
 * ExecutorService:
 *
 *  // 关闭线程池，已提交的任务继续执行，不接受继续提交新任务
 *  void shutdown();
 *
 *  // 关闭线程池，尝试停止正在执行的所有任务，不接受继续提交新任务
 *  // 和前面的相比，区别在于它会去停止当前正在进行的任务
 *  List<Runnable> shutdownNow();
 *
 *  // 线程池是否已关闭
 *  boolean isShutdown();
 *
 *  // 如果调用了 shutdown() 或 shutdownNow() 方法后，所有任务结束了，那么返回 true
 *  // 这个方法必须在调用 shutdown 或 shutdownNow 方法之后调用才会返回 true
 *  boolean isTerminated();
 *
 *  // 等待所有任务完成，并设置超时时间
 *  // 先调用 shutdown 或 shutdownNow，然后再调用这个方法等待所有的线程真正完成，返回值意味着有没有超时
 *  boolean awaitTermination(long timeout, TimeUnit unit);
 *
 *  // 提交一个 Callable 任务
 *  <T> Future <T> submit<Callable<T> task);
 *
 *
 * FutureTask
 *
 *  Future              Runnable
 *      \                   /
 *       \                 /
 *         RunnableFuture
 *               |
 *               |
 *           FutureTask
 *
 *  FutureTask 通过 RunnableFuture 间接实现了 Runnable 接口，所以每个 Runnable 通常都先包装成
 *  FutureTask，然后调用 executor.execute(Runnable command) 将其提交给线程池
 *
 *  <T> Future<T> submit(Runnable task, T result);
 *  Runnable 是没有返回值的，所以，我们需要在 submit 中的第二个参数作为返回值
 *
 *  其实到时候会通过这两个参数，将其包装成 Callable，如果运行时出现异常，call 方法会抛出异常
 *
 * AbstractExecutorService
 *  实现了几个实用的方法，供子类使用
 *
 *  需要获取结果 - FutureTask，用 submit；
 *  不需要获取结果，可以用 execute
 *
 *  protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
 *      return new FutureTask<T>(runnable, value);
 *  }
 *
 * ThreadPoolExecutor:
 *
 *  内部类 Worker，Doug Lea 把线程池中的线程包装成了一个个 Worker，就是线程池中做任务的线程
 *  任务是 Runnable，内部变量名叫 task 或 command，线程是 Worker
 *
 *      private final class Worker extends AbstractQueuedSynchronizer implements Runnable {
 *
 *          // 这个是真正的线程，执行任务靠它
 *          final Thread thread;
 *
 *          // 创建线程的时候，这个线程起来以后需要执行的第一个任务
 *          // 如果为 null，自己到任务队列 BlockingQueue 中取任务
 *          Runnable firstTask;
 *
 *          // 用于存放此线程完成的任务数，volatile 保证了可见性
 *          volatile long completedTasks;
 *
 *
 *          Worker(Runnable firstTask) {
 *              setState(-1); // inhibit interrupts until runWorker
 *              this.firstTask = firstTask;
 *              this.thread = getThreadFactory().newThread(this);
 *          }
 *
 *          public void run() {
 *              runWorker(this);
 *          }
 *
 *  public void execute(Runnable command) {
 *
 *          // 线程池状态 和 线程数
 *         int c = ctl.get();
 *
 *         // 如果当前线程数少于核心线程，那么就直接添加一个 worker 来执行任务
 *         // 创建一个新的线程，并把当前任务 command 作为这个线程的第一个任务 - firstTask
 *         if (workerCountOf(c) < corePoolSize) {
 *              // 添加任务成功了，那么就结束了。
 *              // 至于执行结果，到时候会包装到 FutureTask 中
 *              // 返回 false 代表线程池不允许提交任务
 *             if (addWorker(command, true))
 *                 return;
 *             c = ctl.get();
 *         }
 *
 *         // 到这里说明，要么当前线程数大于等于核心线程数，要么刚刚 addWorker 失败了
 *         // 如果线程池处于 Running 状态，把这个任务添加到任务队列 workQueue 中
 *         if (isRunning(c) && workQueue.offer(command)) {
 *              // 这里面说的是，如果任务进入了 workQueue，我们是否需要开启新的线程
 *              // 因为线程数在 [0, corePoolSize) 是无条件开启线程
 *              // 如果线程数已经大于等于 corePoolSize，那么将任务添加到队列中，然后进到这里
 *             int recheck = ctl.get();
 *             // 如果线程已不在 Running 状态，那么移除已经入队的任务，并且执行拒绝策略
 *             if (! isRunning(recheck) && remove(command))
 *                 reject(command);
 *             // 如果线程池还是 Running，并且线程数为 0，那么开启新的线程
 *             // 这里的真正意图是：担心任务提交到队列中了，但是线程都关闭了
 *             else if (workerCountOf(recheck) == 0)
 *                 addWorker(null, false);
 *         }
 *         // 如果 workQueue 队列满了，那么进入这个分支
 *         // 以 maximumPoolSize 为界创建新的 Worker，
 *         // 如果失败，说明当前线程已经达到 maximumPoolSize，执行拒绝策略
 *         else if (!addWorker(command, false))
 *             reject(command);
 *     }
 *
 *      // 第一个参数是准备提交给这个线程执行的任务，可以为 null
 *      // 第二个参数为 true，代表使用核心线程数 corePoolSize 作为创建线程的界限，也就说创建这个线程的时候，
 *      // 如果线程池中的线程总数已经达到 corePoolSize，那么不能响应这次创建线程的请求
 *      // 如果是 false，代表使用 maximumPoolSize 作为界限
 *
 *
 *      // 其他的不看了，看一下最主要的逻辑
 *
 *      // 把 firstTask 传给 worker 的构造方法
 *      w = new Worker(firstTask);
 *      // Worker 会调用 ThreadFactory 创建一个线程
 *      final Thread t = w.thread;
 *      // 加这个 workers 到 HashSet 中
 *      workers.add(w);
 *      // 启动线程
 *      t.start();
 *
 *      private boolean addWorker(Runnable firstTask, boolean core) {
 *         retry:
 *         for (;;) {
 *             int c = ctl.get();
 *             int rs = runStateOf(c);
 *
 *             // 如果线程已经关闭，并满足以下条件之一，那么不创建新的 worker
 *             // 1. 线程池大于 Shutdown，也就是 stop，tidying，terminated
 *             // 2. firstTask != null
 *             // 3. workQueue.isEmpty()
 *             //
 *             if (rs >= SHUTDOWN &&
 *                 ! (rs == SHUTDOWN &&
 *                    firstTask == null &&
 *                    ! workQueue.isEmpty()))
 *                 return false;
 *
 *             for (;;) {
 *                 int wc = workerCountOf(c);
 *                 if (wc >= CAPACITY ||
 *                     wc >= (core ? corePoolSize : maximumPoolSize))
 *                     return false;
 *                 if (compareAndIncrementWorkerCount(c))
 *                     break retry;
 *                 c = ctl.get();  // Re-read ctl
 *                 if (runStateOf(c) != rs)
 *                     continue retry;
 *                 // else CAS failed due to workerCount change; retry inner loop
 *             }
 *         }
 *
 *         boolean workerStarted = false;
 *         boolean workerAdded = false;
 *         Worker w = null;
 *         try {
 *             w = new Worker(firstTask);
 *             final Thread t = w.thread;
 *             if (t != null) {
 *                 final ReentrantLock mainLock = this.mainLock;
 *                 mainLock.lock();
 *                 try {
 *                     // Recheck while holding lock.
 *                     // Back out on ThreadFactory failure or if
 *                     // shut down before lock acquired.
 *                     int rs = runStateOf(ctl.get());
 *
 *                     if (rs < SHUTDOWN ||
 *                         (rs == SHUTDOWN && firstTask == null)) {
 *                         if (t.isAlive()) // precheck that t is startable
 *                             throw new IllegalThreadStateException();
 *                         workers.add(w);
 *                         int s = workers.size();
 *                         if (s > largestPoolSize)
 *                             largestPoolSize = s;
 *                         workerAdded = true;
 *                     }
 *                 } finally {
 *                     mainLock.unlock();
 *                 }
 *                 if (workerAdded) {
 *                     t.start();
 *                     workerStarted = true;
 *                 }
 *             }
 *         } finally {
 *             if (! workerStarted)
 *                 addWorkerFailed(w);
 *         }
 *         return workerStarted;
 *     }
 *
 *      // 此方法有 worker 线程启动后调用，这里用一个 while 循环来不断地从等待队列中获取任务并执行
 *      // worker 初始化时，可以指定一个 firstTask，那么第一个任务也就不需要从队列中获取
 *     final void runWorker(Worker w) {
 *         Thread wt = Thread.currentThread();
 *         // 该线程的第一个任务
 *         Runnable task = w.firstTask;
 *         w.firstTask = null;
 *         w.unlock(); // allow interrupts
 *         boolean completedAbruptly = true;
 *         try {
 *              // 循环调用 getTask 获取任务
 *             while (task != null || (task = getTask()) != null) {
 *                 w.lock();
 *
 *                 if ((runStateAtLeast(ctl.get(), STOP) ||
 *                      (Thread.interrupted() &&
 *                       runStateAtLeast(ctl.get(), STOP))) &&
 *                     !wt.isInterrupted())
 *                     wt.interrupt();
 *                 try {
 *                     beforeExecute(wt, task);
 *                     Throwable thrown = null;
 *                     try {
 *                          // 执行任务
 *                         task.run();
 *                     } catch (RuntimeException x) {
 *                         thrown = x; throw x;
 *                     } catch (Error x) {
 *                         thrown = x; throw x;
 *                     } catch (Throwable x) {
 *                         thrown = x; throw new Error(x);
 *                     } finally {
 *                         afterExecute(task, thrown);
 *                     }
 *                 } finally {
 *                      // 置空 task，准备 getTask 获取下一个任务
 *                     task = null;
 *                     w.completedTasks++;
 *                     w.unlock();
 *                 }
 *             }
 *             completedAbruptly = false;
 *         } finally {
 *             processWorkerExit(w, completedAbruptly);
 *         }
 *     }
 *
 *      // 此方法有三种可能
 *      // 1. 阻塞知道获取到任务返回。我们知道，核心线程是不会回收的，它们会一直等待任务
 *      // 2. 超时退出。非核心任务 keepAliveTime 应该关闭
 *      // 3. 其他这里先不做分析
 *     private Runnable getTask() {
 *         boolean timedOut = false; // Did the last poll() time out?
 *
 *         for (;;) {
 *             int c = ctl.get();
 *             int rs = runStateOf(c);
 *
 *             // Check if queue empty only if necessary.
 *             if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
 *                 decrementWorkerCount();
 *                 return null;
 *             }
 *
 *             int wc = workerCountOf(c);
 *
 *             // Are workers subject to culling?
 *             boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;
 *
 *             if ((wc > maximumPoolSize || (timed && timedOut))
 *                 && (wc > 1 || workQueue.isEmpty())) {
 *                 if (compareAndDecrementWorkerCount(c))
 *                     return null;
 *                 continue;
 *             }
 *
 *             try {
 *                  // 到 workQueue 中获取任务
 *                 Runnable r = timed ?
 *                     workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
 *                     workQueue.take();
 *                 if (r != null)
 *                     return r;
 *                 timedOut = true;
 *             } catch (InterruptedException retry) {
 *                 timedOut = false;
 *             }
 *         }
 *     }
 *
 *
 * https://www.javadoop.com/post/java-thread-pool
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/26  9:44
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class ParseThreadPoolExecutor {

    public static void main(String[] args) {
        new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), Executors.defaultThreadFactory());
    }

}
