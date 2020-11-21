package com.microwu.concurrent.collection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Description: BlockingQueue
 *  阻塞队列是一个支持两个附加操作的队列。
 *      队列为空时，获取元素的线程等待
 *      队列满时，存储元素的线程会等待
 *
 *       抛出异常  返回特殊值/超时退出  一直阻塞
 *  插入：add          offer               put
 *  移除：remove       poll                take
 *
 *  ArrayBlockingQueue：数组结构的有界阻塞队列
 *  LinkedBlockingQueue：链表接口的有界阻塞队列
 *  PriorityBlockingQueue：优先级排序的无界阻塞队列
 *  DelayQueue：是由优先级队列实现的无界阻塞队列
 *  SynchronousQueue：不存储元素的阻塞队列
 *  LinkedTransferQueue：链表结构的无界阻塞队列
 *  LinkedBlockingDeque：链表结构的双向阻塞队列
 *
 *  ArrayBlockingQueue：数组实现的有界阻塞队列。此队列按照先进先出对元素排序。默认情况下不保证访问者公平的访问队列，通常情况下，保证公平性会减低吞吐量
 *  LinkedBlockingQueue：链表实现的有界阻塞队列，此队列按照先进先出对元素排序
 *  PriorityBlockingQueue： 支持优先级的无界队列。默认情况下采取自然顺序排列
 *  DelayQueue：支持延时获取元素的无界阻塞队列。队列使用 PriorityQueue 来实现。队列中的元素必须实现 Delayed 接口，在创建元素时指定多久才可以获取元素，
 *      只有在延迟期慢的时候才能从队列中提取元素
 *
 *      缓存有效期：DelayQueue 保存缓存元素的有效期，使用一个线程循环查询 DelayQueue，获取到表明缓存有效期到了
 *      定时任务调度：获取到任务，证明任务开始执行了，例如 TimerQueue
 *      参考 ScheduledThreadPoolExecutor 的 ScheduledFutureTask
 *
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/15   15:45
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BlockingQueueTest {

    /**
     *      Main lock guarding all access
     *     final ReentrantLock lock;
     *
     *      Condition for waiting takes
     *     private final Condition notEmpty;
     *
     *      Condition for waiting puts
     *     private final Condition notFull;
     *
     *     public void put(E e) throws InterruptedException {
     *         checkNotNull(e);
     *         final ReentrantLock lock = this.lock;
     *         lock.lockInterruptibly();
     *         try {
     *             while (count == items.length)
     *                 notFull.await();
     *             enqueue(e);
     *             // notEmpty.signal();
     *         } finally {
     *             lock.unlock();
     *         }
     *     }
     *
     *     public E take() throws InterruptedException {
     *         final ReentrantLock lock = this.lock;
     *         lock.lockInterruptibly();
     *         try {
     *             while (count == 0)
     *                 notEmpty.await();
     *             return dequeue();
     *         } finally {
     *             lock.unlock();
     *         }
     *     }
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/15  16:18
     *
     * @param
     * @return  void
     */
    public static void test() {
        // 有界
        ArrayBlockingQueue<String> strings = new ArrayBlockingQueue<String>(10);
        strings.offer("a");
        String poll = strings.poll();
        System.out.println(poll);
    }

    /**
     * 延时队列实现
     *
     *  long delay = first.getDelay(TimeUnit.NANOSECONDS);
     *     if (delay <= 0)
     *         return q.poll();
     *     else if (leader != null)
     *         available.await();
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/15  16:17
     *
     * @param
     * @return  void
     */
    public static void test02() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    }

    public static void main(String[] args) {

    }
}