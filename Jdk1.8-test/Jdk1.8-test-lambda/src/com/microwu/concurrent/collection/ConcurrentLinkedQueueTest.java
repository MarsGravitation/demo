package com.microwu.concurrent.collection;

/**
 * Description: ConcurrentLinkedQueue
 *  实现线程安全的队列有两种方式：一种是阻塞式算法，一种是非阻塞式算法。
 *  使用阻塞算法的队列可以用一个锁（入队和出队使用同一把锁）或两把锁（入队和出队用不同的锁）等方式来实现。
 *  非阻塞式的实现是使用循环 CAS 的方式来实现。
 *
 *  ConcurrentLinkedQueue：FIFO 单向队列
 *  ConcurrentLinkedDeque：双向队列
 *
 * 1. 数据结构
 *  private transient volatile Node<E> head;
 *  private transient volatile Node<E> tail;
 *
 *  private static class Node<E> {
 *      volatile E item;
 *      volatile Node<E> next;
 *  }
 *
 *  ConcurrentLinkedQueue 由 head 和 tail 节点组成，每个节点由节点元素和指向下一个节点的引用组成
 *
 * 2. 入队 offer
 *  入队就是将入队节点添加到队列尾部
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/20   11:06
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConcurrentLinkedQueueTest {
}