package com.mocrowu.cxd.listener;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/24   11:08
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TestListener {
    public static void main(String[] args) {
        Person person = new Person();
        person.registerListener(new MyPersonLister());
        person.run();
        person.eat();
    }

    static class MyPersonLister implements PersonListener {

        @Override
        public void dorun(Even even) {
            // 拿到事件源
            Person person = even.getPerson();
            System.out.println("人在跑之前执行的动作。。。");
        }

        @Override
        public void doeat(Even even) {
            Person person = even.getPerson();
            System.out.println("人在吃之前执行的动作。。。");

        }
    }
}