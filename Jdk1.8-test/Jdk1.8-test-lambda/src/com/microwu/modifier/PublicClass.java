package com.microwu.modifier;

import com.microwu.modifier.protect.ProtectedClass;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/3/26   20:49
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PublicClass {

    public int a = 1;

    public static void main(String[] args) {
        ProtectedClass protectedClass = new ProtectedClass();
        // protected 只能当前类，同包或子类访问
        // default(friendly) 当前类，同包
        // private 当前类
//        System.out.println(protectedClass.a);
    }
}