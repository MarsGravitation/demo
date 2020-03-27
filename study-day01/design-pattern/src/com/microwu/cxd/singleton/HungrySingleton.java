package com.microwu.cxd.singleton;

/**
 * Description: 饿汉模式
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/3   15:08
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HungrySingleton {
    private static HungrySingleton instance = new HungrySingleton();

    private HungrySingleton(){}

    public static HungrySingleton getInstance(){
        return instance;
    }
}