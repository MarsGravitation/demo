package com.mocrowu.cxd.listener;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/24   11:01
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Person {
    // 首先定义一个私有的、空的监听器对象，用来接受传递进来的事件监听器
    private PersonListener listener;

    // 将传递进来的事件监听器赋给listener
    public void registerListener(PersonListener listener) {
        this.listener = listener;
    }

    public void run() {
        if (listener != null) {
            Even even = new Even(this);
            this.listener.dorun(even);
        }
        System.out.println("人具有跑的方法。。。");
    }

    public void eat() {
        if(listener != null) {
            Even even = new Even(this);
            this.listener.doeat(even);
        }
        System.out.println("人具有吃的方法。。。");
    }
}