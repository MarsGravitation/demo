package com.microwu.cxd.singleton;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/3   16:06
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class StaticInnerSingleton {
    private StaticInnerSingleton(){};

    public static final StaticInnerSingleton getInstance(){
        return SingletonHolder.INSTACE;
    }

    private static class SingletonHolder{
        private static final StaticInnerSingleton INSTACE = new StaticInnerSingleton();
    }
}