package com.microwu.cxd.network.netty.ds.atomic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/11  15:40
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class AtomicIntegerTest {

    private static final Unsafe unsafe = reflectGetUnsafe();
    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset(AtomicIntegerTest.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

    private static Unsafe reflectGetUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
//            log.error(e.getMessage(), e);
            return null;
        }
    }

    private volatile int value;

    public AtomicIntegerTest(int initialValue) {
        value = initialValue;
    }

    public AtomicIntegerTest() {

    }

    public final int get() {
        return value;
    }

    public final void set(int newValue) {
        value = newValue;
    }

    /**
     *
     * 为什么这里使用 getIntVolatile，而不使用  getInt？
     *
     * unsafe 不知道在 valueOffset 哪里有一个 volatile 领域，volatile 可以防止重排序
     *
     * 如果更换为 getInt，编译器可能会进行重排序，由于这里没有 volatile 语义，编译器可以讲读取移除循环
     *
     * int v = getInt(o, offset);
     *  do {
     *  } while (!weakCompareAndSetInt(o, offset, v, v + delta));
     *
     *  这样会导致死循环
     *
     *     public final int getAndSetInt(Object var1, long var2, int var4) {
     *         int var5;
     *         do {
     *             var5 = this.getIntVolatile(var1, var2);
     *         } while(!this.compareAndSwapInt(var1, var2, var5, var4));
     *
     *         return var5;
     *     }
     *
     * https://stackoverflow.com/questions/64375201/replace-getintvolatileobject-var1-long-var2-with-getintobject-var1-long
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/11     15:47
     *
     * @param newValue
     * @return int
     */
    public final int getAndSet(int newValue) {
        return unsafe.getAndSetInt(this, valueOffset, newValue);
    }

    /**
     * public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/11     15:48
     *
     * @param expect
     * @param update
     * @return boolean
     */
    public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }

    /**
     * public final int getAndAddInt(Object var1, long var2, int var4) {
     *         int var5;
     *         do {
     *             var5 = this.getIntVolatile(var1, var2);
     *         } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
     *
     *         return var5;
     *     }
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/11     15:48
     *
     * @param
     * @return int
     */
    public final int getAndIncrement() {
        return unsafe.getAndAddInt(this, valueOffset, 1);
    }

}
