package com.microwu.cxd.queue.disruptor.blog;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

/**
 * Description: 单生产者多消费者，多消费者间形成依赖关系，每个依赖节点只有一个消费者
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/25   14:39
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DisruptorMain2 {

    public static void main(String[] args) throws InterruptedException {
        EventFactory<Order> factory = new OrderFactory();
        int ringBufferSize = 1024 * 1024;
        Disruptor<Order> disruptor = new Disruptor<>(factory, ringBufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
        // 多消费者间形成依赖关系，每个依赖节点的消费者为单线程
        disruptor.handleEventsWith(new OrderHandler1("1")).then(new OrderHandler1("2"), new OrderHandler1("3")).then(new OrderHandler1("4"));
        disruptor.start();
        RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
        Producer producer = new Producer(ringBuffer);
        // 单生产者，生产 3 条数据
        for (int i = 0; i < 3; i++) {
            producer.onData(i + " ");
        }

        Thread.sleep(1000);
        disruptor.shutdown();
    }

}