package com.microwu.cxd.singleton;

/**
 * Description: 双重校验
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/3   15:11
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DoubleCheckedLockingSingleton {
    private volatile static DoubleCheckedLockingSingleton instance;
    private DoubleCheckedLockingSingleton(){}

    public static DoubleCheckedLockingSingleton getInstance(){
        if(instance == null){
            synchronized (DoubleCheckedLockingSingleton.class){
                if(instance == null){
                    instance = new DoubleCheckedLockingSingleton();
                }
            }
        }
        return instance;
    }
}