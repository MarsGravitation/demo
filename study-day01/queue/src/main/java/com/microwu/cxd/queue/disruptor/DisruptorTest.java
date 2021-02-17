package com.microwu.cxd.queue.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;

/**
 * Description: 没 10 ms 向 disruptor 中插入一个元素，消费者读取数据，并打印到终端
 *
 * https://tech.meituan.com/2016/11/18/disruptor.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/11   16:05
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DisruptorTest {

    public static void main(String[] args) throws InterruptedException {
        // 队列中的元素
        class Element {
            private int value;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        // 生产者的线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "simpleThread");
            }
        };

        // RingBuffer 生产工厂，初始化 RingBuffer 的时候使用
        EventFactory<Element> factory = new EventFactory<Element>() {
            @Override
            public Element newInstance() {
                return new Element();
            }
        };

        // 处理 Event 的 handler
        EventHandler<Element> handler = new EventHandler<Element>() {
            @Override
            public void onEvent(Element element, long l, boolean b) throws Exception {
                System.out.println("Element: " + element.getValue());
            }
        };

        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        // 指定 RingBuffer 大小
        int  bufferSize = 16;

        // 创建 disruptor，采用单生产者模式
        Disruptor<Element> disruptor = new Disruptor<>(factory, bufferSize, threadFactory, ProducerType.SINGLE, strategy);

        // 设置 EventHandler
        disruptor.handleEventsWith(handler);

        // 启动 disruptor 的线程
        disruptor.start();

        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();
        for (int i = 0; true; i++) {
            // 获取下一个可用位置的下标
            long sequence = ringBuffer.next();
            System.out.println(sequence);
            try {
                // 返回可用位置的元素
                Element event = ringBuffer.get(sequence);
                // 设置该元素的值
                event.setValue(1);
            } finally {
                ringBuffer.publish(sequence);
            }

            Thread.sleep(1000);
        }

    }

}