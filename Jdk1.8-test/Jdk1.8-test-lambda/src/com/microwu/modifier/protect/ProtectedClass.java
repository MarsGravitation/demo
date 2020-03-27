package com.microwu.modifier.protect;

import com.microwu.modifier.PublicClass;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/3/26   20:50
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ProtectedClass {

    protected int a = 1;

    public static void main(String[] args) {
        PublicClass publicClass = new PublicClass();
        System.out.println(publicClass.a);
    }
}