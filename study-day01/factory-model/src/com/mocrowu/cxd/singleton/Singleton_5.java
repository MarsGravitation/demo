package com.mocrowu.cxd.singleton;

/**
 * 静态内部类：
 *      它的机制和懒汉模式类似，都是使用类加载机制，但是它使用了内部静态类，
 *      如果不调用内部类，就不会加载，从而实现懒汉式的延迟加载
 */
public class Singleton_5 {

    private Singleton_5() {
    }

    private static class SingletonHolder{
        private static Singleton_5 singleton_5 = new Singleton_5();
    }

    public Singleton_5 getInstance(){
        return SingletonHolder.singleton_5;
    }
}
