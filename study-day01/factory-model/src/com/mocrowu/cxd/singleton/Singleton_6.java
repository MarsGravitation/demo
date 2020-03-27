package com.mocrowu.cxd.singleton;

/**
 *  以上的四种方法存在的缺陷：
 *      1.序列化和反序列化会创建多个对象
 *      2.通过反射也可以创建多个对象
 *  使用枚举可以保证线程安全和反射调用构造器以外，还提供了自动序列化机制
 *  缺点：很少有人使用
 */
public enum Singleton_6 {
    instance;
    public void whateverMethod(){}

}
