package com.microwu.concurrent.collection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BlockingQueue
 *  BlockingQueue 是一个先进先出的队列 - Queue，为什么是阻塞 Blocking 呢？是因为 BlockingQueue 支持当获取队列元素但是队列为空时
 *  ，会阻塞，等待队列中有元素再返回；也支持添加元素，如果队列已满，阻塞，直到队列不满为止
 *
 *  BlockingQueue 是一个接口，继承自 Queue，所以其实现类也可以作为 Queue 的实现来使用，而 Queue 又
 *  继承自 Collection 接口。
 *
 *  BlockingQueue 对插入、移除、获取元素提供了四种不同的方法用于不同的场景中：
 *      1. 抛出异常
 *      2. 返回特殊值 null - true/false
 *      3. 阻塞
 *      4. 阻塞或者超过时间
 *
 *  Throws exception    Special Value   Blocks  Times out
 *      add                 offer       put     offer(e, time, unit)
 *      remove              poll        take    poll(time, unit)
 *      element             peek        -       -
 *
 *      BlockingQueue 不接受 null，会抛出异常。因为 null 通常用于作为特殊值的返回，代表 poll 失败
 *
 *      BlockingQueue 有界/无界
 *
 *      BlockingQueue 是设计用来实现生产者-消费者队列的
 *
 *      BlockingQueue 的实现都是线程安全的，但是批量的集合操作不一定是原子操作
 *
 *  ArrayBlockingQueue: 有界队列的实现类，底层采用数组来实现，其并发控制采用可重入锁来控制，不管是插入还是读取，都需要获取到锁才能进行操作
 *      // 用于存放元素的数组
 *      final Object[] items;
 *      // 下一次读取操作的位置
 *      int takeIndex;
 *      // 下一次写入操作的位置
 *      int putIndex;
 *      // 队列中的元素数量
 *      int count;
 *
 *      // 以下几个就是控制并发用的同步器
 *      final ReentrantLock lock;
 *      private final Condition notEmpty;
 *      private final Condition notFull;
 *
 *      // 代码比较简单 - 阻塞
 *      // 简单描述一下 MESA 管程的范式
 *      // 必须获取互斥锁
 *      // wait 必须放在 while 循环中
 *      public void put(E e) throws InterruptedException {
 *         checkNotNull(e);
 *         // 加锁
 *         final ReentrantLock lock = this.lock;
 *         lock.lockInterruptibly();
 *         try {
 *             // 队列已满
 *             while (count == items.length)
 *                  // 进入等待队列
 *                 notFull.await();
 *             // 队列不满，入队
 *             enqueue(e);
 *         } finally {
 *             lock.unlock();
 *         }
 *     }
 *
 *      // 入队
 *      // 看样子，应该是一个环形的数组
 *      // 如果 index 到数组尾部，就从头开始
 *     private void enqueue(E x) {
 *         final Object[] items = this.items;
 *         items[putIndex] = x;
 *         if (++putIndex == items.length)
 *             putIndex = 0;
 *         count++;
 *         // 唤醒非空的等待队列的线程
 *         notEmpty.signal();
 *     }
 *
 *  LinkedBlockingQueue: 基于单项链表实现的阻塞队列，可以当作无界队列也可以当作有界队列
 *
 *      public LinkedBlockingQueue() {
 *          this(Integer.MAX_VALUE);
 *      }
 *
 *      public LinkedBlockingQueue(int capacity) {
 *          if (capacity <= 0) throw new IllegalArgumentException();
 *          this.capacity = capacity;
 *          last = head = new Node<E>(null);
 *      }
 *
 *      // 队列容量
 *      private final int capacity;
 *
 *      // 队列中的元素数量
 *      private final AtomicInteger count = new AtomicInteger(0);
 *
 *      // 队头
 *      private transient Node<E> head;
 *
 *      // 队尾
 *      private transient Node<E> last;
 *
 *      // take、poll、peek 等读操作的方法需要获取到这个锁
 *      private final ReentrantLock takeLock = new ReentrantLock();
 *
 *      // 如果读操作的时候队列是空的，那么等待 notEmpty 条件
 *      private final Condition notEmpty = takeLock.newCondition();
 *
 *      // put，offer 等写操作的方法需要获取到这个锁
 *      private final ReentrantLock putLock = new ReentrantLock();
 *
 *      // 如果写操作的时候队列是满的，那么等待 notFull 条件
 *      private final Condition notFull = putLock.newCondition();
 *
 *      takeLock 和 notEmpty 搭配：如果要 take 一个元素，需要获取 takeLock 锁，但是获取了锁还不够，如果队列此时为空，还需要队列不为空 notEmpty
 *      putLock 需要和 notFull 搭配：如果 put 一个元素，需要获取 putLock 锁，如果队列此时已满，还需要队列不是 notFull 这个条件
 *
 * // 初始化一个空的头结点，那么第一个元素入队时，队列中就会有两个元素
 * // 读取元素时，也就是获取头结点后面的一个节点
 * public LinkedBlockingQueue(int capacity) {
 *     if (capacity <= 0) throw new IllegalArgumentException();
 *     this.capacity = capacity;
 *     last = head = new Node<E>(null);
 * }
 *
 *     public void put(E e) throws InterruptedException {
 *         if (e == null) throw new NullPointerException();
 *         int c = -1;
 *         Node<E> node = new Node<E>(e);
 *         final ReentrantLock putLock = this.putLock;
 *         final AtomicInteger count = this.count;
 *         // 获取到 putLock 锁
 *         putLock.lockInterruptibly();
 *         try {
 *              // 如果队列满，等待 notFull 的条件满足
 *             while (count.get() == capacity) {
 *                 notFull.await();
 *             }
 *             // 入队
 *             enqueue(node);
 *             // count 原子 + 1，c 还是加一前的值
 *             c = count.getAndIncrement();
 *             // 如果元素入队后，还有一个槽可以使用，调用 notFull.signal 唤醒等待线程
 *             // 那些线程会等待着 notFull 这个 Condition 呢？
 *             if (c + 1 < capacity)
 *                 notFull.signal();
 *         } finally {
 *              // 解锁
 *             putLock.unlock();
 *         }
 *         // 如果 c == 0，那么代表队列在这个元素入队前是空的（不包括 head 空节点）
 *         // 那么所有的毒县城都在等待 notEmpty 这个条件，等待唤醒，这里做一次唤醒操作
 *         if (c == 0)
 *             signalNotEmpty();
 *     }
 *
 *    // 入队就是将 last 属性指向这个新元素，并且让原队尾的 next 指向这个元素
 *    // 这里入队没有并发问题，因为只有获取到 putLock 独占锁以后，才可以进行此操作
 *    private void enqueue(Node<E> node) {
 *         // assert putLock.isHeldByCurrentThread();
 *         // assert last.next == null;
 *         last = last.next = node;
 *     }
 *
 *     // 元素入队后，如果需要，调用这个方法唤醒读线程来读
 *     private void signalNotEmpty() {
 *         final ReentrantLock takeLock = this.takeLock;
 *         takeLock.lock();
 *         try {
 *             notEmpty.signal();
 *         } finally {
 *             takeLock.unlock();
 *         }
 *     }
 *
 *     // take 方法
 *     public E take() throws InterruptedException {
 *         E x;
 *         int c = -1;
 *         final AtomicInteger count = this.count;
 *         final ReentrantLock takeLock = this.takeLock;
 *         // 首先获取 takeLock 锁
 *         takeLock.lockInterruptibly();
 *         try {
 *              // 如果队列为空，需要等待直到队列不为空
 *             while (count.get() == 0) {
 *                 notEmpty.await();
 *             }
 *             // 出队
 *             x = dequeue();
 *             // 获取到出队前的 count
 *             c = count.getAndDecrement();
 *             // 如果出队前，count 大于 1
 *             if (c > 1)
 *                  // 唤醒 notEmpty
 *                 notEmpty.signal();
 *         } finally {
 *              // 释放锁
 *             takeLock.unlock();
 *         }
 *
 *         // 如果出队前 c == capacity，那么说明在这个 take 方法发生的时候，队列是满的
 *         // 既然出队了一个，那么意味着队列不满了，唤醒写携程去写
 *         if (c == capacity)
 *             signalNotFull();
 *         return x;
 *     }
 *
 *     private E dequeue() {
 *          // 头结点是空的
 *         Node<E> h = head;
 *         // 获取到第一个节点
 *         Node<E> first = h.next;
 *         h.next = h; // help GC
 *         // 设置 first 为头结点
 *         head = first;
 *         // 获取 first 的元素
 *         E x = first.item;
 *         // 将头结点置为 null
 *         first.item = null;
 *         return x;
 *     }
 *
 * 问题：为什么 ArrayBlockingQueue 是一个锁，而 LinkedBlockingQueue 是两个锁？
 *
 *  a. 他们之所以没有使用它，主要是因为实现的复杂性，尤其是迭代器，并且在复杂性和性能增益之间进行权衡并没有那么有利可图。
 *  b. 可能是 Doug Lea 不觉得 Java 需要支持 2 个不同的 BlockingQueues，它们只是在分配方案上有所不同。
 *
 * https://blog.csdn.net/liubenlong007/article/details/102823081
 *
 *
 *
 * https://www.javadoop.com/post/java-concurrent-queue
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/26  15:53
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class BlockingQueueTest02 {

    public static void main(String[] args) {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(1);
        LinkedBlockingQueue<String> strings = new LinkedBlockingQueue<>(1);
    }

}
