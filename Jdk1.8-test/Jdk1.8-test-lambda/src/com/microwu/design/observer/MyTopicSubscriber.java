package com.microwu.design.observer;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/28   15:08
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyTopicSubscriber implements Observer {

    private String name;
    private Subject subject;

    public MyTopicSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void update() {
        String msg = (String) subject.getUpdate(this);
        if (msg == null) {
            System.out.println(name + " no new message");
        } else {
            System.out.println(name + "consuming message " + msg);
        }
    }

    @Override
    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}