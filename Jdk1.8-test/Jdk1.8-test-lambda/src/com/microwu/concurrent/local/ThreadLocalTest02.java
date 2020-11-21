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
 *  3. 内存泄漏：
 *      ThreadLocal 本身不存放任何数据，而 ThreadLocal 中的数据实际上是存放在线程实例的，从实际上来看是线程内存泄漏，底层来看是 Thread 对象的成员变量 threadLocals 持有
 *      大量 K-V 结构，并且线程一直处于活跃状态导致变量 threadLocals 无法释放被回收。threadLocals 持有大量 K-V 结构这一点的前提是要存在大量的 ThreadLocal 实例的定义，一般
 *      来说，一个应用不可能定义大量的 ThreadLocal，所以一般的泄漏源是线程一直处于活跃状态导致变量的 threadLocals 无法释放被回收。
 *      但是我们知道，ThreadLocalMap.Entry 结构中的 key 用到了弱引用，每次 GC 会回收这些 key，此时 ThreadLocalMap 会出现一些 key = null，但是 value 不为 null 的数据，如果
 *      不及时清理，就会一直驻留在 ThreadLocalMap 中。这也是 ThreadLocal get, set, remove 都会清理 key = null 的代码块，所以，内存泄漏可能出现的地方：
 *          a. 大量的初始化 ThreadLocal 实例，初始化后不再调用 get, set, remove 方法
 *          b. 初始化了大量的 ThreadLocal 实例，这些 ThreadLocal 存放了容量大的 value，并且使用这些 ThreadLocal 的线程实例一直处于活跃状态
 *
 *      ThreadLocal 设计亮点是 key 用到了弱引用。如果强饮用的话，ThreadLocalMap 所有数据都是和 Thread 生命周期绑定，这样很容易出现因为大量线程持续活跃导致的内存泄漏。使用了
 *      弱引用的话，JVM 触发 GC 回收弱引用后，下一次 get, set, remove 可以删除 null 的值，起到了惰性删除释放内存的作用
 *
 *  4. 使用场景
 *      4.1 ThreadLocal 用作保存每个线程独享的对象，为每个线程都创建一个副本，这样每个线程都可以修改自己所拥有的副本，不会影响其他线程的副本，确保了线程安全
 *          |- 通常用在保存线程不安全的工具类上，典型的就是 SimpleDateFormat
 *          class ThreadSafeFormatter{
 *              public static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal
 *                  = ThreadLocal.withInitial(() -> new SimpleDateFormat("mm:ss"));
 *          }
 *      4.2 每个线程内需要独立保存信息，以便供其他方法更方便的获取该信息的场景。每个线程获取到的信息可能都是不一样的，前面执行了保存，后面可以直接获取到，避免了传参，类似于全局变量的概念
 *          |- 保存用户权限信息，同一个线程内相同，不同的线程不同
 *          class UserContextHolder {
 *              // 创建ThreadLocal保存User对象
 *              public static ThreadLocal<User> holder = new ThreadLocal<>();
 *          }
 *
 *         |- Spring Security 中的 SecurityContextPersistenceFilter
 *         请求开始把信息保存到 ThreadLocal 中，请求结束清除内容
 *         SecurityContextHolder.setContext(contextBeforeChainExecution);
 *         chain.doFilter(holder.getRequest(), holder.getResponse());
 *         SecurityContextHolder.clearContext();
 *
 *      https://www.cnblogs.com/zz-ksw/p/12684877.html
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