package com.mocrowu.cxd.singleton;

/**
 *  双重检查加锁
 *  为什么？ --- 优化锁方法，锁方法的方式，每次创建对象时都要加锁，而现在只有当对象为空的时候才加锁
 *  缺点：逻辑比较复杂，双重if判断，容易出错,而且由于指令重排序优化，需要加valatile关键字
 *
 */
public class Singleton_4 {
    private static volatile Singleton_4 singleton_4;

    private Singleton_4() {
    }

    public static Singleton_4 getInstance(){
        if(singleton_4 == null){
            synchronized (Singleton_4.class){
                if(singleton_4 == null){
                    singleton_4 = new Singleton_4();
                }
            }
        }
        return singleton_4;
    }
}
