package com.microwu.jdk8.lambda;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/8/6   10:02
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface StaticInterface {
    static void staticMethod() {
        System.out.println("static method");
    }

    default void print() {
        System.out.println("hello default");
    }
}
