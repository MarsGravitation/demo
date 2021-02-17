package com.microwu.cxd.queue.disruptor.blog;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

/**
 * Description: 单生产者多消费者，多消费者对消息 m 独立消费
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/25   14:53
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DisruptorMain4 {

    public static void main(String[] args) throws InterruptedException {
        EventFactory<Order> factory = new OrderFactory();
        int ringBufferSize = 1024 * 1024;
        Disruptor<Order> disruptor = new Disruptor<>(factory, ringBufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
        // 设置一个消费者
        disruptor.handleEventsWith(new OrderHandler1("1"), new OrderHandler1("2"));
        disruptor.start();
        RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
        Producer producer = new Producer(ringBuffer);
        // 单生产者，生产 3 条数据
        for (int i = 0; i < 3; i++) {
            producer.onData(i + " ");
        }

        // 为了保证消费者线程已经启动，留足足够的时间
        Thread.sleep(1000);
        disruptor.shutdown();
    }

}