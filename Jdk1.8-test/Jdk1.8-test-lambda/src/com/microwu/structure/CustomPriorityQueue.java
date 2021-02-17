package com.microwu.structure;

import java.util.StringJoiner;

/**
 * Description: 小顶堆（优先队列） - 数组实现
 * 思路：
 *  左叶子节点 = 父节点下标 * 2 + 1
 *  右叶子节点 = 父节点下标 * 2 + 2
 *  父节点 = （叶子节点 - 1）/ 2
 *
 *  0    1    2
 *  父   左   右
 *
 * 代码见 PriorityQueue
 * 图解：https://blog.csdn.net/hrn1216/article/details/51465270
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/14   14:57
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CustomPriorityQueue<E> {

    private Object[] queue;

    private int size;

    /**
     * 这里主要考虑 0 的问题
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/14  16:28
     *
     * @param   	e
     * @return  void
     */
    public void add(E e) {
        if (size == 0) {
            queue = new Object[10];
            queue[0] = e;
        } else {
            siftUp(size, e);
        }
        size++;
    }

    /**
     * 向上调整
     * 首先加入二叉树的最后一个节点，从底向上调整
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/14  16:28
     *
     * @param   	k
     * @param 		e
     * @return  void
     */
    private void siftUp(int k, E e) {
//        Comparable key = (Comparable) e;
//        while (k > 0) {
//            // 获取父节点
//            int parent = (k - 1) >>> 1;
//            Object o = queue[parent];
//            // 父节点比子节点大的话，不需要调整
//            if (key.compareTo((E) o) >= 0) {
//                break;
//            }
//            // 父节点比子节点小，将父节点赋值给子节点
//            queue[k] = o;
//            k = parent;
//        }
//        queue[k] = e;
    }

    /**
     * 删除节点，把第一个节点取出来，获取最后一个节点，
     * 将最后一个节点放到第一个节点进行调整，最后一个节点置为 null，垃圾回收
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/14  16:30
     *
     * @param
     * @return  E
     */
    public E remove() {
        // 取出第一位
        E result = (E) queue[0];
        E e = (E) queue[--size];
        queue[size] = null;
        if (size != 0) {
            siftDown(0, e);
        }
        return result;
    }

    /**
     * 向下调整
     * 和左右子节点进行比较，将最小的调整到上面，这里面调整到一半就可以了
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/14  16:31
     *
     * @param   	k
     * @param 		e
     * @return  void
     */
    private void siftDown(int k, E e) {
//        Comparable key = (Comparable) e;
//        int half = size >>> 1;
//        while (k < half) {
//            int left = (k << 1) + 1;
//            Object c = queue[left];
//            int right = left + 1;
//            if (right < size && ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0) {
//                c = queue[left = right];
//            }
//            if (key.compareTo((E) c) <= 0) {
//                break;
//            }
//            queue[k] = c;
//            k = left;
//        }
//        queue[k] = e;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(",");
        for (int i = 0; i < size; i++) {
            sj.add(queue[i].toString());
        }
        return sj.toString();
    }

    public static void main(String[] args) {
//        PriorityQueue<String> priorityQueue = new PriorityQueue<>();
//        priorityQueue.remove();

        CustomPriorityQueue<Integer> priorityQueue = new CustomPriorityQueue<>();
        priorityQueue.add(8);
        priorityQueue.add(7);
        priorityQueue.add(6);
        priorityQueue.add(5);
        priorityQueue.add(4);
        priorityQueue.add(3);
        priorityQueue.add(2);
        priorityQueue.add(1);
        System.out.println(priorityQueue.toString());
        while (priorityQueue.size > 0) {
            System.out.println(priorityQueue.remove());
        }
    }

}