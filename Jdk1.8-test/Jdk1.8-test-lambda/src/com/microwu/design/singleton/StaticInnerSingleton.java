package com.microwu.design.singleton;

/**
 * Description: 静态内部类
 *  利用 JVM 隐含的同步操作，如：
 *      1. 有静态初始化器（在静态字段上或者 static 块中初始化器）初始化数据时
 *      2. 访问 final 字段
 *      3. 在创建线程之前创建对象
 *      4. 线程可以看见它要处理的对象
 *
 *  在静态内部类里去创建本类（外部类）的对象，只要是不使用这个静态内部类，就不创建对象实例，从而实现延迟加载和线程安全
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/26   9:47
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class StaticInnerSingleton {

    private StaticInnerSingleton() {

    }

    /**
     * 在静态内部类里去创建外部类对象
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/11/26  9:55
     *
     * @param
     * @return  com.microwu.design.singleton.StaticInnerSingleton
     */
    public static StaticInnerSingleton getInstance() {
        return Holder.SINGLETON;
    }

    /**
     * 静态内部类相当于外部类的 static 域，它的对象和外部类对象间不存在依赖关系，因此可以直接创建
     * 因为静态内部类相当于外部类的静态成员，所以在第一次被使用的时候才被装载，且只装载一次
     */
    private static class Holder {
        // 内部类的对象实例是绑定在外部对象实例中的
        // 静态内部类可以定义静态方法，在静态方法中能够引用外部类中静态成员方法或者成员变量
        // 使用静态初始化器来实现线程安全的单例类，它由 JVM 来保证线程安全
        private static final StaticInnerSingleton SINGLETON = new StaticInnerSingleton();
    }

}