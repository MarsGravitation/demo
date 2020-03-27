package com.mocrowu.cxd.singleton;

/**
 *  单例模式：
 *      1.构造方法私有化
 *      2.提供共有的静态方法供外界获取对象
 *  问题：
 *      线程不安全
 */
public class Singleton_1 {
    private static Singleton_1 singleton_1;
    // 构造方法私有化
    private Singleton_1() {
    }

    public Singleton_1 getInstance(){
        if(singleton_1 == null){
            singleton_1 = new Singleton_1();
        }
        return singleton_1;
    }
}
