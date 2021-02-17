package com.microwu.cxd.queue.disruptor.blog;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * Description:
 *  在 disruptor 框架调用 start 方法之前，往往需要将消息的消费者指定给 disruptor 框架
 *  handleEventsWith -> 传入多个消费者，封装成一个组。每个消费者都会消费，不存在竞争
 *  handleEventsWithWorkerPool -> 同样。同一个组对同一条消息不重复消费
 *  根据消费者集合是否独立消费消息，可以对不同接口进行实现，也可以对两种接口同时实现，具体消费流程有 disruptor 方法调用决定
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/25   14:24
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class OrderHandler1 implements EventHandler<Order>, WorkHandler<Order> {

    private String consumerId;

    public OrderHandler1(String consumerId) {
        this.consumerId = consumerId;
    }

    @Override
    public void onEvent(Order order, long l, boolean b) throws Exception {
        System.out.println(Thread.currentThread().getName() + ": OrderHandler1 --> " + this.consumerId + ", 消费消息：" + order.getId());
    }

    @Override
    public void onEvent(Order order) throws Exception {
        System.out.println("OrderHandler1 -> " + this.consumerId + ", 消费消息：" + order.getId());
    }
}