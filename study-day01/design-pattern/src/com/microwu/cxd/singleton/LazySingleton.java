package com.microwu.cxd.singleton;

/**
 * Description:
 *  1.构造函数私有化
 *  2.提供一个静态方法供外界访问
 *  3.将实例对象静态
 *
 *  缺点：线程不安全
 *  解决办法：加锁
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/3   15:02
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LazySingleton {
    private static LazySingleton lazySingleton;
    private LazySingleton(){

    }

    public static synchronized LazySingleton getLazySingleton(){
        if(lazySingleton == null){
            return new LazySingleton();
        }
        return lazySingleton;
    }
}