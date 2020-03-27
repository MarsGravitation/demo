package com.microwu.inner;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/5   14:31
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class InnerClassDemo2 {
    private int a;


    // 普通内部类
    private class DemoRunnable implements Runnable {
        @Override
        public void run() {

        }
    }

    // 匿名对象
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    public void method() {
        int b = 1;
        // 局部内部类
        class InnerRunnable implements Runnable {
            @Override
            public void run() {
                System.out.println(a + b);
            }
        }

        InnerRunnable innerRunnable = new InnerRunnable();
    }

    public static void main(String[] args) {

    }
}