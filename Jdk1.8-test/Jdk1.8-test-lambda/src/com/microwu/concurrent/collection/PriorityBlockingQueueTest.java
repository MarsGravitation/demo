package com.microwu.concurrent.collection;

/**
 * Description: PriorityBlockingQueue
 * PriorityBlockingQueue 是带优先级的无界阻塞队列，每次出队都返回优先级最高的元素，是二叉树最小堆的实现。
 * PriorityBlockingQueue 数据结构和 PriorityQueue 一致，而线程安全性使用的是 ReentrantLock
 *
 * 1. 基本属性
 *  // 最大可分配队列容量 Integer.MAX_VALUE - 8，减 8 是因为有的 VM 实现在数组头有些内容
 *  private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
 *  // 默认队列容量11，这里不是 HashMap，不需要 hash 取余，因此不必是 2^n
 *  private static final int DEFAULT_INITIAL_CAPACITY = 11;
 *
 *  // 数组结构，是二叉树最小堆的实现
 *  private transient Object[] queue;
 *  private transient int size;
 *
 *  PriorityBlockingQueue  使用 ReentrantLock 保证数据安全性，数据结构使用的是数组。PriorityBlockingQueue 数组的结构和 PriorityQueue 一致，
 *  是基于平衡二叉堆实现，父节点下标是 n，左节点则是 2n + 1，右节点是 2n + 2。0 是最小的元素。
 *
 * 2. 入队
 *  两件事：判断是否需要扩容；将元素插入数组中
 * 3. 出队
 *  array[0] 永远是最小的元素
 * 4. 数据结构
 *  // 数组结构，是二叉树最小堆的实现，array[0] 永远是优先级最高的元素
 *  private transient Object[] queue;
 *
 *  // offer 时将元素 e 插入到节点 n 位置
 *  siftUpComparable(n, e, array);
 *  // poll 时将最后一个元素 array[n] 插入到 0 位置
 *  siftDownComparable(0, x, array, n);
 *
 *  PriorityQueue 是一个完全二叉树，且不允许出现 null 节点，器父节点都比叶子节点小，这个堆排序中的小顶堆。二叉树存入数组的方式很简单，就是从上到下，从左到右。
 *  完全二叉树可以和数组中的位置一一对应：
 *      左叶子节点 = 父节点下标 * 2 + 1
 *      右叶子节点 = 父节点下标 * 2 + 2
 *      父节点 = （叶子节点 - 1）/ 2
 *
 *
 * 题外话：Comparable 接口
 *  int compareTo(T o)
 *  将此对象与指定对象进行比较。当此对象小于返回负整数，等于返回0，大于返回正整数
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/20   11:29
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PriorityBlockingQueueTest {

    public static void main(String[] args) {
//        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
//        priorityQueue.add(2);
//        priorityQueue.add(1);
        CustomPriorityQueue<Integer> priorityQueue = new CustomPriorityQueue<>();
        priorityQueue.add(2);
        priorityQueue.add(1);
    }

    private static class CustomPriorityQueue<E> {
        /**
         * 默认容量为 10，不支持扩容
         */
        private Object[] queue = new Object[10];

        private int size;

        public boolean add(E e) {
            if (size == 0) {
                queue[size] = e;
            } else {
                siftUp(size, e);
            }
            size++;
            return true;
        }

        /**
         * 思路：将要插入的元素 x 和它的父节点对比，如果比父节点大就一直向上移动
         *
         * @author   chengxudong               chengxudong@microwu.com
         * @date    2020/10/20  14:02
         *
         * @param   	k
         * @param 		e
         * @return  void
         */
        @SuppressWarnings("unchecked")
        private void siftUp(int k, E e) {
            // key = 新元素
            // k = 下标
            Comparable<? super E> key = (Comparable<? super E>) e;
            while (k > 0) {
                // 父节点下标
                int parent = (k - 1) >>> 1;
                // o 父节点
                Object o = queue[parent];
                if (key.compareTo((E) o) >= 0) {
                    break;
                }
                // 如果新元素比父节点小
                //
                queue[k] = o;
                k = parent;
            }
            queue[k] = key;
        }

        @SuppressWarnings("unchecked")
        public E remove() {
            int s = --size;
            E result = (E) queue[0];
            E x = (E) queue[s];
            queue[s] = null;
            if (s != 0) {
                siftDown(0, x);
            }
            return result;
        }

        /**
         * 思路：将元素 x 和第 0 位的左右子节点进行比较，如果 x 大于这两个子节点则向下移动，小的子节点向上移
         *
         * @author   chengxudong               chengxudong@microwu.com
         * @date    2020/10/20  14:50
         *
         * @param
         * @return  E
         */
        @SuppressWarnings("unchecked")
        private void siftDown(int k, E x) {
            Comparable<? super E> key = (Comparable<? super E>) x;
            int half = size >>> 1;
            while (k < half) {
                int child = (k << 1) + 1;
                Object c = queue[child];
                int right = child + 1;
                if (right < size && ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0) {
                    c = queue[child = right];
                }
                if (key.compareTo((E) c) <= 0) {
                    break;
                }
                queue[k] = c;
                k = child;
            }
            queue[k] = key;

        }
    }
}