package com.microwu.cxd.queue.disruptor.blog;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

/**
 * Description: 单生产者，多消费者模式。多消费者对于消息不重复消费
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/25   14:49
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DisruptorMain3 {

    public static void main(String[] args) throws InterruptedException {
        EventFactory<Order> factory = new OrderFactory();
        int ringBufferSize = 1024 * 1024;
        Disruptor<Order> disruptor = new Disruptor<>(factory, ringBufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());

        disruptor.handleEventsWithWorkerPool(new OrderHandler1("1"), new OrderHandler1("2"));
        disruptor.start();

        RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
        Producer producer = new Producer(ringBuffer);
        for (int i = 0; i < 3; i++) {
            producer.onData(i + " ");
        }

        Thread.sleep(1000);
        disruptor.shutdown();
    }

}