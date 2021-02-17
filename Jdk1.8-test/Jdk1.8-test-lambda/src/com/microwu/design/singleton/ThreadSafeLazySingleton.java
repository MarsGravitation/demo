package com.microwu.design.singleton;

/**
 * Description: 双重校验
 *  - 注意 volatile 关键字
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/26   9:37
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ThreadSafeLazySingleton {

    private static volatile ThreadSafeLazySingleton instance = null;

    private ThreadSafeLazySingleton() {

    }

    public static ThreadSafeLazySingleton getInstance() {
        if (instance == null) {
            synchronized (ThreadSafeLazySingleton.class) {
                if (instance == null) {
                    instance = new ThreadSafeLazySingleton();
                }
            }
        }
        return instance;
    }

}