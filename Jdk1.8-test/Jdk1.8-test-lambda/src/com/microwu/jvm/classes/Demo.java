package com.microwu.jvm.classes;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/26   16:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Demo {

//    public static int doStaticMethod(int i, long l, float f, Object o, byte b) {
//        return 0;
//    }

    public static int add(int a, int b) {
        int c = 0;
        c = a + b;
        return c;
    }

    public static void main(String[] args) {
        add(1, 2);
    }

}