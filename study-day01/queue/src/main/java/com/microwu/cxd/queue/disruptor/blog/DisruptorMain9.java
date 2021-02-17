package com.microwu.cxd.queue.disruptor.blog;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * Description: 多生产者，单消费者模式
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/25   15:09
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DisruptorMain9 {

    public static void main(String[] args) throws InterruptedException {
        EventFactory<Order> factory = new OrderFactory();
        int ringBufferSize = 1024 * 1024;
        Disruptor<Order> disruptor = new Disruptor<>(factory, ringBufferSize, Executors.defaultThreadFactory(), ProducerType.MULTI, new YieldingWaitStrategy());
        disruptor.handleEventsWith(new OrderHandler1("1"));
        disruptor.start();

        final RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
        final CountDownLatch countDownLatch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            new Thread() {
                @Override
                public void run() {
                    Producer producer = new Producer(ringBuffer);
                    producer.onData("1");
                    countDownLatch.countDown();
                }
            }.start();
        }

        countDownLatch.await();
        Thread.sleep(1000);
        disruptor.shutdown();

    }

}