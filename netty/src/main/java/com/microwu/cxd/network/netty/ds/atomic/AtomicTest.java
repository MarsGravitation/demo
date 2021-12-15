package com.microwu.cxd.network.netty.ds.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Netty 为什么不直接用 AtomicXXX，而要用 AtomicXXXFieldUpdater去更新变量呢？
 *
 * AtomicInteger 核心源码
 *
 * private volatile int value;
 *
 * public AtomicInteger(int initialValue) {
 *     value = initialValue;
 * }
 *
 * public final int get() {
 *     return value;
 * }
 *
 * // 自增 1，并返回自增之前的值
 * public final int getAdnIncrement() {
 *     return unsafe.getAndAddInt(this, valueOffset, 1);
 * }
 *
 * 为啥还额外提供一套院子更新器呢？
 *  |- 基于 AtomicIntegerFieldUpdater 实现的院子计数器，比单纯的直接用 AtomicInteger 包装 int 变量的花销要小，
 *      因为前者只需要一个全局的静态变量 AtomicIntegerFieldUpdater 即可包装 volatile 修饰的非静态变量，
 *      然后配合 CAS 就能实现原子更新，而这样做，使得后续同一个类的每个对象只需要共享这个静态的院子更新器即可为对象计数器
 *      实现原子更新，而原子类视为同一个类的每个对象都创建了一个计数器 + AtomicInteger  对象
 *
 *  |- 使用原子更新器，不会破坏共享变量原来的结构
 *
 *  原子更新器是静态的，但是其修饰的共享变量却是类的对象属性，即每个类的对象仍然是只包含自己那独一份的共享变量，不会因为原子更新器
 *  是静态的，而受到任何影响
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247484505&idx=1&sn=8343bd6fa644e52904eb7cc9a807a48d&chksm=fbc09259ccb71b4fc3ce267731f9d28f2e0c140e152e1a5968408f25845bde83115a5855b47e&scene=21#wechat_redirect
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/10  19:52
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class AtomicTest {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();

//        return value;
        atomicInteger.get();

//        value = newValue;
        atomicInteger.set(1);

//        int var5;
//        do {
//            var5 = this.getIntVolatile(var1, var2);
//        } while(!this.compareAndSwapInt(var1, var2, var5, var4));
//
//        return var5;
        atomicInteger.getAndSet(1);

//        int var5;
//        do {
//            var5 = this.getIntVolatile(var1, var2);
//        } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
//
//        return var5;
        atomicInteger.getAndIncrement();

    }

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 启动
     *
     * 启动 NIO 线程之前会做一次是否已经启动的判断，避免重复启动
     *
     * 当本 NIO 线程实例没有启动时，会做一次 CAS 计算，注意 CAS 对应操作系统的一个指令，
     * 是原子操作，如果多个外部线程在启动 NIO 线程，那么同时只有一个外部线程能启动成功，
     * 后续的线程不会重复启动这个 NIO 线程。保证在 NIO 线程的一次生命周期内，外部线程
     * 只能调用一次 doStartThread 方法，这样可以实现无锁更新，且没有自旋，这里之所以
     * 不需要自旋，是因为启动线程应该是一锤子买卖，启动不成功，说明已经启动过了
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/10     20:17
     *
     * @param
     * @return void
     */
    public void start() {
        if (atomicInteger.get() == 1) {
            return;
        }

        if (atomicInteger.compareAndSet(0, 1)) {
            System.out.println("启动成功。。。");
        }
    }

    /**
     * 注意事项：
     *  1. 关闭不是一锤子买卖，需要自旋重试，直到 CAS 操作成功
     *  2. 需要使用局部变量缓存外部的共享变量的旧值，保证 CAS 操作执行期间
     *  该共享变量的旧值不被外部线程修改
     *  3. 每次执行 CAS 操作之前，先判断一次旧值，符合条件才执行 CAS 操作，否则
     *  说明已经被其他线程更新成功，无需重复操作
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/11     16:23
     *
     * @param
     * @return void
     */
    public void stop() {
        for (;;) {
            int oldState = atomicInteger.get();
            if (oldState == 0 || atomicInteger.compareAndSet(1, 0)) {
                break;
            }
        }
    }

}
