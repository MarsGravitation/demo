package com.microwu.concurrent.collection;

/**
 * Description: LinkedBlockingQueue
 *  1. 数据结构
 *      LinkedBlockingQueue 和 ConcurrentLinkedQueue 一样，和 ConcurrentLinkedQueue 不同的是 LinkedBlockingQueue 是基于 ReentrantLock 实现的，
 *      因此 head，last 以及 item，next 都不用 volatile 修饰
 *
 *      // head.item == null
 *      transient Node<E> head;
 *      // last.next == null
 *      private transient Node<E> last;
 *
 *      private static class Node<E> {
 *          E item;
 *          Node<E> next;
 *      }
 *
 *  2. 基于 ReentrantLock 实现
 *      private final ReentrantLock takeLock = new ReentrantLock();
 *      // 集合已空则调用notEmpty.await，等集合添加元素后调用notEmpty.signal
 *      private final Condition notEmpty = takeLock.newCondition();
 *
 *      private final ReentrantLock putLock = new ReentrantLock();
 *      // 集合已满则调用notFull.await，等集合取出元素后调用notFull.signal
 *      private final Condition notFull = putLock.newCondition();
 *
 *  3. 入队 offer
 *      lase 是实时指向尾节点的
 *
 *     putLock.lock();
 *     try {
 *     	// 2. 元素入队有2个操作：一是元素添加到last.next并更新last；
 *     	//    二是唤醒阻塞的put操作继续添加元素(只有put时会阻塞notFull.await)
 *         if (count.get() < capacity) {
 *         	// 2.1 元素入队
 *             enqueue(node);
 *         	// 2.2 c表示插入前元素的个数
 *             c = count.getAndIncrement();
 *         	// 2.3 集合未满，唤醒put操作，继续添加元素
 *             if (c + 1 < capacity)
 *                 notFull.signal();
 *         }
 *     } finally {
 *         putLock.unlock();
 *     }
 *
 *  4. 出队 poll
 *     takeLock.lock();
 *     try {
 *     	// 2. 元素出队有2个操作：一是head.next出队
 *     	//    二是唤醒阻塞的take操作继续取出元素(只有take时会阻塞notEmpty.await)
 *         if (count.get() > 0) {
 *             // 2.1 head.next出队
 *             x = dequeue();
 *             // 2.2 c为poll前元素的个数
 *             c = count.getAndDecrement();
 *             // 2.3 集合中元素不为空，唤醒take操作，断续取元素
 *             if (c > 1)
 *                 notEmpty.signal();
 *         }
 *     } finally {
 *         takeLock.unlock();
 *     }
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/20   11:19
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LinkedBlockingQueueTest {
    public static void main(String[] args) {

    }
}