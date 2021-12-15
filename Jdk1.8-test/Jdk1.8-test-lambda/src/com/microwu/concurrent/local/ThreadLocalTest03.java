package com.microwu.concurrent.local;

import java.lang.ref.WeakReference;

/**
 *
 * ThreadLocal 内存泄漏
 *  key 的 弱引用，Entry 的 key 为什么可能为 null，还有清理 Entry 的操作
 *
 *  如果一个对象没有强引用，只有弱引用的话，这个对象是活不过一次 GC 的，
 *  所以这样的设计就是为了让当外部没有对 ThreadLocal 对象有强引用的时候，
 *  可以将 ThreadLocal 对象给清理掉
 *
 *      栈                           堆
 *  线程引用(GC Root) -> 线程对象 -> ThreadLocalMap -> Entry
 *  ThreadLocal 引用 -> ThreadLocal 对象 ---> Entry.Key
 *
 *  线程在我们的引用中，常常是以线程池的方式来使用的，所以这个引用链会一直存在
 *  另一条引用链，如果栈退出了，栈和 ThreadLocal 的强引用没了，而和 Entry
 *  之间属于弱引用，此时发生 GC 就可以被回收
 *
 *  但是可能存在内存泄漏 --- 程序中已经无用的内存无法释放，造成系统内存的浪费
 *
 *  Entry 中的 Key 即 ThreadLocal 对象被回收了，会发生 Entry 中 key 为
 *  null 的情况，其实这个 Entry 就已经没用了，但是又无法回收，因为有 Thread
 *  -> ThreadLocalMap -> Entry 这条强引用在，这样没用的内存无法被回收
 *
 *  清理无用的 Entry
 *      getEntryAfterMiss
 *      rehash
 *
 * https://mp.weixin.qq.com/s/seT9Qruo3X8QxIFW5xaJCA
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/9/8  10:05
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class ThreadLocalTest03 {

    public static void main(String[] args) {
        test02();
    }

    public static void test() {
        ThreadLocal<String> local = new ThreadLocal<>();
        local.set("cxd");
        System.out.println(local.get());
    }

    public static void test02() {
        // WeakReference<Car> weakCar = new WeakReference(Car)(car);
        WeakReference<Person> reference = new WeakReference<>(new Person());
        // 获取 reference 引用的 Object 时
        System.out.println(reference.get());
        System.gc();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(reference.get());
    }

    private static class Person {

        @Override
        public String toString() {
            return "i am a person";
        }
    }

}
