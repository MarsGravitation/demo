package com.microwu.concurrent.future;

/**
 * Description:
 *
 * https://juejin.im/post/6844904199625375757#heading-11
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/2   11:21
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class FutureTaskDemo03 {

    /**
     * 类结构分析
     *  FutureTask 实现了 RunnableFuture 接口，而 RunnableFuture 接口又继承了 Future 和 Runnable 接口。
     *  因此 FutureTask 可以作为任务被线程 Thread 执行，同时还可以获得任务执行的结果
     *
     *  Future 接口：
     *      cancel： 尝试取消线程任务的执行
     *          a. 如果线程任务已经完成或已经取消或其他原因不能取消，此时会失败并返回 false
     *          b. 如果任务还未开始执行，那么任务将被取消执行，此时返回 true
     *          c. 如果任务已经开始执行，那么 mayInterruptIfRunning 参数将决定是否取消任务执行
     *          这里并不能真正取消线程任务的执行，而是发出一个线程中断信号，一般需要结合 Thread.currentThread().isInterrupted
     *           来使用
     *       isCancelled：是否取消
     *       isDone：任务停止、异常、取消返回 true
     *       get：阻塞等待获取任务执行结果
     *       get(timeout, unit): 如果规定时间未获取到结果，抛出超时异常
     *
     * 成员变量：
     *  callable：异步任务
     *  outcome：异步执行结果
     *  volatile runner：用来执行 callable 的线程
     *  volatile waiters：线程等待节点，reiber statck 的一种实现
     *  volatile state：任务执行状态
     *
     *  stateOffset：state 偏移地址
     *  runnerOffset：runner 偏移地址
     *  waitersOffset：waiters 偏移地址
     *
     * 构造函数：
     *  this.callable = callable;
     *  this.state = NEW
     *  新建 FutureTask，此时任务状态 state = NEW
     *
     * run 方法：
     *  1. 为了防止多线程并发执行异步任务，这里需要判断线程满不满足执行异步任务的条件
     *      1.1 state = NEW && runner == null，此时可以执行
     *      1.2 state == NEW && runner != null,说明有线程正在执行，直接返回
     *      1.3 如果 state != NEW,此时不管 runner，直接返回，因为已经有 runner 执行过了
     *
     *      state != new || !UNSAFE.cas(this, runnerOffset, null, Thread.currentThread())
     *      return;
     *
     *  2. 调用 callable
     *  3. 根据异步结果做不同的处理
     *  4. 善后工作
     *  注意：判断 runner 是否为 null 是调用 UNSAFE 的 CAS 方法判断的，同时 runner 被修饰为 volatile，
     *          保证设值能够立即刷进主存，可被其他线程可见
     *
     * set 方法
     *  UNSAFE.cas(this, stateOffset, NEW, COMPLETING)
     *  如果状态为 NEW，则设置为 COMPLETING
     *  什么情况下 state != NEW，因为此任务只可能被一个线程执行，答案是有人调用了 cancel，这时什么也不需要做
     *
     *  setException 这里不分析了
     *
     * finishCompletion 方法
     *  不论正常还是异常结束，都要唤醒阻塞的线程，这里指的是我们调用 get 方法的那个线程
     *  (q = waiters) != null
     *  等待线程链表头结点，如果不为空，此时以后进先出移除节点；如果为空，说明没有线程 get 结果，什么也不做
     *
     *  将waiters 设置为空
     *  UNSAFE.cas(this, waiterOffset, q, null)
     *  唤醒正在阻塞的线程
     *  q.thread != null; LockSupport.unpark(t)
     *  获取下一个节点，如果不为空，则继续唤醒
     *  q = q.next;
     *
     * get 方法
     *  state 小于 completing，说明任务正在执行过程中
     *  s <= COMPLETING awaitDone
     *  最后根据任务状态来返回结果，此时有三种结果 a.正常 b.异常 c.取消
     *  report(s)
     *
     * awaitDone 方法
     *  如果当前任务被打断，移除 waitNode 节点，抛出打断异常
     *  a. q == null;q = new waitNode();
     *      将当前线程包装成 WaitNode
     *  b. queued = UNSAFE.cas(this, waitersOffset, q.next = waiters, q);
     *      若当前线程还未入线程等待队列，加入头部
     *  c. LockSupport.park(this)
     *      阻塞当前线程
     *  d. s == completing Thread.yield();
     *      如果任务正在执行中，get 线程让出 CPU
     *  e. s > completing return
     *      返回执行结果
     *
     *  总结：
     *      0. 死循环
     *      1. 如果当前线程被中断，移除该线程 WaitNode 链表节点，抛出打断异常
     *      2. s > completing; return
     *      3. s = completing; Thread.yield(); get 线程让出 CPU 执行权
     *      4. q = null， 说明当前线程还未设置 WaitNode ，新建节点
     *      5. queued = false, 当前线程还未加入等待链表，此时加入链表头部
     *      6. 超时逻辑这里不详细分析
     *      7. 前面的逻辑都不符合，阻塞当前线程
     *
     *  注意：这里是一个死循环，一个线程进来多次进入不同的分支
     *
     * report 方法：
     *  根据 state 返回结果
     *
     * cancel 方法：
     *  1. 判断当前状态，若 s = new, 根据 mayInterruptIfRunning 赋值给 state
     *      a. 当任务状态不为 new，说明异步任务已经完成，或者抛出异常，直接返回 false
     *      b. 当 state = new，且参数为 true，设置为 INTERRUPTING，否则为 INTERRUPTED
     *      c. 如果 mayInterruptInRunning = true，此时中断执行异步任务的线程
     *          runner.interrupt();
     *          UNSAFE.po(this, stateOffset, INTERRUPTED);
     *      d. 最后调用 finishCompletion 唤醒阻塞的线程
     *
     *  总结：
     *      mayInterruptInRunning = false, state = cancelled
     *      mayInterruptInRunning = true, state: new -> ing -> ed
     *      该方法并不能真正中断异步线程，如果处于 sleep、wait、join，抛出 InterruptedException
     *      如果处于 while 循环时，可以使用 Thread.currentThread().isInterrupted() 结束任务
     *
     * 总结：
     *  1. 实现 callable 接口，定义业务逻辑
     *  2. 把 callable 传给 FutureTask，然后 FutureTask 作为异步任务交给线程执行
     *  3. 内部维护了一个 state，任何操作都是围绕这个状态进行的
     *  4. 只能有一个线程异步执行
     *  5. 可以有多个线程获取结果，若结果未完成，则加入等待队列
     *  6. 异步任务结束后，唤醒等待线程，队列是一个栈结构，先进后出
     *  7. cancel 并不能真正停止异步线程，只能发出中断信号
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/2  11:21
     *
     * @param
     * @return  void
     */
    public static void test() {

    }
}