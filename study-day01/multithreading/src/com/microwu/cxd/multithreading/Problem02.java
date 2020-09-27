package com.microwu.cxd.multithreading;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/1   10:12
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Problem02 {

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                int num = 0;
                for (int j = 0; j < 10000; j++) {
                    if (j == 0) {
                        num = increase(j);
                    } else {
                        num = increase(num);
                    }
                }
                System.out.println(Thread.currentThread() + ":" + num);
            }).start();
        }
    }

    public static int increase(int i) {
        return ++i;
    }
}