package com.microwu.concurrent.future;

import sun.misc.Unsafe;

/**
 * Description:
 *  park: 阻塞当前线程，直到调用 LockSupport.unpark(thread)，或者进行线程中断
 *      若线程是通过中断的方式唤醒，则不会抛出异常，且中断表示不会更改，
 *      需要调用 Thread.interrputed 才能清除
 *  park(Object object): 和上面的差不多，只是价格 object，在 jstack 时，有了这个对象方便定位问题
 *      不是对这个对象阻塞，只是当前线程阻塞，这个 object 只是定位问题的参数
 *  unpark(Thread): 唤醒线程
 *
 *
 *  https://www.cnblogs.com/skywang12345/
 *  http://zhanjindong.com/
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/24   11:39
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Node<E> {

    volatile E item;
    volatile Node<E> next;

    Node(E item) {
        // 将 node 对象的指定 itemOffset 偏移量设置一个引用值
        unsafe.putObject(this, itemOffset, item);
    }

    boolean casItem(E cmp, E val) {
        // 院子更新 item 值
        return unsafe.compareAndSwapObject(this, itemOffset, cmp, val);
    }

    void lazySetNext(Node<E> val) {
        // 调用这个方法和 putObject 差不多，只是这个方法设置后对应的值的可见性不一定得到保证
        // 这个方法能起这个作用，通常是作用在 volatile field 上，也就是说，下面的参数 val 是被 volatile 修饰的
        unsafe.putOrderedObject(this, nextOffset, val);
    }

    boolean casNext(Node<E> cmp, Node<E> val) {
        return unsafe.compareAndSwapObject(this, nextOffset, cmp, val);
    }

    private static Unsafe unsafe;
    private static long itemOffset;
    private static long nextOffset;

    static {
        try {
            unsafe = Unsafe.getUnsafe();
            Class<Node> k = Node.class;
            itemOffset = unsafe.objectFieldOffset(k.getDeclaredField("item"));
            nextOffset = unsafe.objectFieldOffset(k.getDeclaredField("next"));
        } catch (Exception e) {

        }
    }
}