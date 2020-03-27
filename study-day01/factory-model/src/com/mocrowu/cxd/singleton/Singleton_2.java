package com.mocrowu.cxd.singleton;

/**
 *  线程安全的单例模式一：饿汉式
 *      创建私有静态属性的时候就创建对象
 *  问题：
 *      类加载的时候就创建对象，比较浪费资源
 */
public class Singleton_2 {
    private static Singleton_2 singleton_2 = new Singleton_2();

    private Singleton_2() {
    }

    public Singleton_2 getInstance(){
        return singleton_2;
    }
}
