package com.microwu.concurrent.aqs;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Description: https://juejin.im/post/6864210697292054541#heading-7
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/1   15:02
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CLHLock {

    /**
     * CLH 锁节点
     */
    private static class CLHNode {
        /**
         * 锁状态，默认为 false，表示线程没有获取到锁；true 表示线程获取到锁或正在等待
         * 为了保证 locked 状态是线程间可见的，用 volatile 修饰
         */
        volatile boolean locked = false;
    }

    /**
     * 尾节点，总是指向最后一个 CLHNode 节点
     * 注意：这里使用了 Java 原子系列值 AtomicReference 来保证原子更新
     */
    private final AtomicReference<CLHNode> tailNode;

    /**
     * 当前节点的前继节点
     */
    private final ThreadLocal<CLHNode> preNode;

    /**
     * 当前节点
     */
    private final ThreadLocal<CLHNode> curNode;

    public CLHLock() {
        // 初始化时尾节点指向一个空的 CLH 节点
        tailNode = new AtomicReference<>(new CLHNode());
        // 初始化当前的 CLH 节点
        curNode = new ThreadLocal<CLHNode>(){
            @Override
            protected CLHNode initialValue() {
                return new CLHNode();
            }
        };
        // 初始化前继节点，注意此时前继节点没有存储 CLHNode 对象，存储的是 null
        preNode = new ThreadLocal<>();
    }

    /**
     * 获取锁
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/1  15:11
     *
     * @param
     * @return  void
     */
    public void lock() {
        // 取出当前线程 ThreadLocal 存储的当前节点，初始化值总是一个新建的 CLHNode，locked 状态为 false
        CLHNode currNode = curNode.get();
        // 此时把 lock 设置为 true，表示一个有效状态
        // 即获取到了锁或正在等待锁的状态
        currNode.locked = true;
        // 当一个线程到来时，总是将尾节点取出来赋值当前线程的前继节点
        // 然后再把当前线程的当前节点赋值给尾节点
        // 注意：在多线程并发情况下，通过 AtomicReference 能防止并发问题
        // 注意：那个线程先执行到这里就会先执行 preNode.set(preNode);因此构建了一条逻辑线程等待链
        // 这条链避免了线程饥饿现象发生
        CLHNode preNode = tailNode.getAndSet(currNode);
        // 刚获取的尾节点 - 前一线程的当前节点 赋值给当前线程的前继节点 ThreadLocal
        this.preNode.set(preNode);
        // 1. 若前继节点的 locked = false，表示获取到了锁，不用自旋等待
        // 2. 若前继节点的 locked = true，表示前一线程获取到了锁或者正在等待，自旋等待
        while (preNode.locked) {
            System.out.println("线程 " + Thread.currentThread() + " 没能获取到锁，进行自旋等待");
        }
        // 执行到这里，说明当前线程获取了锁
        System.out.println("线程 " + Thread.currentThread() + " 获取到了锁！！！");

    }

    public void unLock() {
        // 获取当前线程的当前节点
        CLHNode node = curNode.get();
        // 进行解锁操作
        // 这里将 locked 置为 false，此时执行了 lock 方法正在自旋等待的后继节点将会获取到锁
        // 注意：而不是所有正在自旋等待的线程去并发竞争锁
        node.locked = false;
        System.out.println("线程 " + Thread.currentThread() + " 释放了锁！！！");

        CLHNode newCurNode = new CLHNode();
        curNode.set(newCurNode);

        // 能提高 GC 效率和节省内存空间
        this.curNode.set(preNode.get());
    }

    private static int cnt = 0;

    public static void main(String[] args) throws InterruptedException {
        final CLHLock lock = new CLHLock();

        for (int i = 0; i< 10; i++) {
            new Thread(() -> {
                lock.lock();

                cnt++;

                lock.unLock();
            }).start();
        }

        Thread.sleep(10000);
        System.out.println(cnt);
    }

}