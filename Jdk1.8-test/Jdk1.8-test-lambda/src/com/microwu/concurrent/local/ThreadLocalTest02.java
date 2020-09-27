package com.microwu.concurrent.local;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 *  1. ThreadLocal 原理：
 *      ThreadLocal 实例总是通过 Thread.currentThread 获取到当前操作线程实例，然后去操作线程实例中的
 *      ThreadLocalMap 类型的成员变量，因此它是一个桥梁，本身不具备存储功能
 *
 *  2. ThreadLocal 源码分析
 *      2.1 内部属性
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/1   11:18
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ThreadLocalTest02 {

    private static final ThreadLocal<String> LOCAL = new ThreadLocal<>();

    public static void test() {
        LOCAL.set("doge");
        System.out.println(LOCAL.get());
    }

    /**
     * //获取下一个ThreadLocal实例的哈希魔数
     * private final int threadLocalHashCode = nextHashCode();
     *
     * //原子计数器，主要到它被定义为静态
     * private static AtomicInteger nextHashCode = new AtomicInteger();
     *
     * //哈希魔数(增长数)，也是带符号的32位整型值黄金分割值的取正
     * private static final int HASH_INCREMENT = 0x61c88647;
     *
     * //生成下一个哈希魔数
     * private static int nextHashCode() {
     *     return nextHashCode.getAndAdd(HASH_INCREMENT);
     * }
     *
     * threadLocalHashCode 是一个 final 属性，而原子计数器变量 nextHashCode 和生成下一个哈希魔数的方法
     * nextHashCode 是静态变量和静态方法，静态变量只会初始化一次。换而言之，每新建一个 ThreadLocal 实例，
     * 内部 threadLocalHashCode 就会增加 0x61c88647
     *
     * threadLocalHashCode 是下面的 ThreadLocalMap 结构中使用的哈希算法的核心变量，对于每个 ThreadLocal 实例，
     * 他的 threadLocalHashCode 是唯一的
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/1  13:47
     *
     * @param
     * @return  void
     */
    public static void test02() {
        new ThreadLocal<>();
        new ThreadLocal<>();
        new ThreadLocal<>();
    }

    /**
     * getAndAdd：原子 增加给定的值到当前的值，返回先前的值
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/1  13:43
     *
     * @param
     * @return  void
     */
    public static void test03() {
        AtomicInteger atomicInteger = new AtomicInteger();
        System.out.println(atomicInteger);
        int andAdd = atomicInteger.getAndAdd(1);
        System.out.println(andAdd);
        System.out.println(atomicInteger);
    }

    /**
     * ThreadLocalMap 是一个定制的散列映射，仅适用于维护线程本地变量。
     * 它的所有方法都是定义在 ThreadLocal 类之内。
     * 它的包是默认，所以在 Thread 类中可以定义 ThreadLocalMap 作为变量
     * 为了处理非常大（值）和长时间的用途，哈希表的 key 使用了弱引用
     * 引用的队列不在被使用的时候，对应的过期条目就能通过主动删除移除哈希表
     *
     * getEntry - 通过当前 ThreadLocal 实例获取哈希表中对应的 Entry
     * set - ThreadLocal 作为 key，对当前的哈希表设置值
     *
     * 简单来说，ThreadLocalMap 是 ThreadLocal 真正的数据存储容器，实际上 ThreadLocal 数据操作的
     * 复杂部分的所有逻辑都在 ThreadLocalMap 中进行，而ThreadLocalMap 实例是 Thread 的成员变量，
     * 在 Thread#set 方法首次调用的时候设置到当前执行的线程实例中。如果在同一个线程中使用多个 ThreadLocal 实例，
     *
     * (实际上，每个 ThreadLocal 实例对应的是 ThreadLocalMap 的哈希表中的一个哈希槽) - 没太理解
     *
     * entry table
     *
     * |---------|---------|---------|---------|---------|---------|
     * |  ...    | Long(1) |  ...   |Integer(1)|   ...   |String(1)|
     * |---------|---------|---------|---------|---------|---------|
     *
     * ThreadLocal 的创建
     *  构造函数没有任何操作，只是为了得到一个 ThreadLocal 的泛型实例，后续作为 ThreadLocalMap$Entry 的值
     * ThreadLocal#set
     *  1. 获取当前运行线程的实例
     *  2. 通过线程实例获取线程实例成员 threadLocals，如果为 null，则创建一个 ThreadLocalMap 赋值给 threadLocals
     *  3. 通过 threadLocals 设置 value，如果已存在，则进行覆盖
     *
     * 内存泄漏问题：
     *
     * 最佳实践：
     *  每次使用完 ThreadLocal 实例，都调用它的 remove 方法，清除 Entry 中的数据
     *  调用 remove 最佳时机是线程运行结束前的 finally 代码块中
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/1  13:52
     *
     * @param   	
     * @return  void
     */
    public static void test04() {
        ThreadLocal<Object> tl1 = new ThreadLocal<>();
        ThreadLocal<Object> tl2 = new ThreadLocal<>();
        ThreadLocal<Object> tl3 = new ThreadLocal<>();

        tl1.set(1);
        tl2.set("1");
        tl3.set(1L);
    }

    public static void main(String[] args) {
//        test02();
//        test03();
        test04();
    }
}