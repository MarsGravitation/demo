package com.mocrowu.cxd.singleton;

/**
 *  懒汉式：使用锁机制
 *  缺点：
 *      效率太低
 */
public class Singleton_3 {
    private static Singleton_3 singleton_3;
    private Singleton_3(){

    }
    public static synchronized Singleton_3 getInstance(){
        if(singleton_3 == null){
            singleton_3 = new Singleton_3();
        }
        return singleton_3;
    }
}
