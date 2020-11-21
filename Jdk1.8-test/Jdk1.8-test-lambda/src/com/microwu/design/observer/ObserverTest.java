package com.microwu.design.observer;

/**
 * Description:
 *  Subject 被观察者包含了一些需要在其状态改变时通知的观察者。提供注册和注销方法。当 Subject 被观察者发生变化时，通知所有的观察者。
 *      当通知观察者时，可以推送更新内容，或者提供另外一个方法来获取更新内容
 *  Observer 观察者应该有一种方法，能后设置被观察者并且可以由被观察者使用来通知其更新
 *
 *  总结：被观察者有一个观察者的引用集合，有信息，观察者有被观察者的引用
 *      被观察者可以注册观察者，当信息改变时，调用观察者的方法，观察者可以通过被观察者的引用查看最新的信息
 *
 * http://ifeve.com/observer-design-pattern-in-java-example-tutorial/
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/28   14:47
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ObserverTest {
    public static void main(String[] args) {
        MyTopic topic = new MyTopic();

        MyTopicSubscriber obj1 = new MyTopicSubscriber("Obj1");
        MyTopicSubscriber obj2 = new MyTopicSubscriber("Obj2");
        MyTopicSubscriber obj3 = new MyTopicSubscriber("Obj3");

        topic.register(obj1);
        topic.register(obj2);
        topic.register(obj3);

        obj1.setSubject(topic);
        obj2.setSubject(topic);
        obj3.setSubject(topic);

        obj1.update();

        topic.postMessage("new message");

    }
}