package com.microwu.cxd.queue.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

/**
 * Description: Disruptor
 * 1. 核心概念
 *  1.1 环形队列 ringBuffer
 *      数据缓冲区，不同线程之间传递数据的 BUFFER。RingBuffer 是存储消息的地方
 *  1.2 Producer/Consumer
 *      Producer 生产 event 数据，EventHandler 作为消费者消费 event 并进行逻辑处理。消费消息的进度通过 Sequence 来控制
 *  1.3 Sequence
 *      是一个递增的序号，就是一个计数器
 *  1.4 Sequence Barrier
 *  1.5 Wait Strategy
 *      等待策略
 *  1.6 Event
 *      生产者和消费者之间进行交换的数据被称为 Event
 *  1.7 EventProcessor
 *  1.8 EventHandler
 *      消费者
 *
 * http://ifeve.com/disruptor-info/
 *
 * 2. Disruptor 为什么这么快？
 *  2.1 RingBuffer
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/16   13:45
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DisruptorDemo {

    /**
     * 把 long 从生产者传递给消费者，消费者打印出该值
     */
    static class LongEvent {
        private long value;
    }

    static class ObjectEvent<T> {
        T val;

        void clear() {
            val = null;
        }
    }

    static class ClearingEventHandler<T> implements EventHandler<ObjectEvent<T>> {

        @Override
        public void onEvent(ObjectEvent<T> event, long l, boolean b) throws Exception {
            event.clear();
        }
    }

    /**
     * 由于需要让 Disruptor 为我们创建事件，声明一个 EventFactory 来实例化 Event 对象
     */
    static class LongEventFactory implements EventFactory<LongEvent> {

        @Override
        public LongEvent newInstance() {
            return new LongEvent();
        }
    }

    /**
     * 事件消费者
     */
    static class LongEventHandler implements EventHandler<LongEvent> {

        @Override
        public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
            System.out.println("Event: " + event.value);
        }
    }

    /**
     * 使用翻译器发布
     */
    static class LongEventProducerWithTranslator {
        private final RingBuffer<LongEvent> ringBuffer;

        LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer) {
            this.ringBuffer = ringBuffer;
        }

        private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR = new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
            @Override
            public void translateTo(LongEvent event, long sequence, ByteBuffer byteBuffer) {
                event.value = byteBuffer.getLong(0);
            }
        };

        public void onData(ByteBuffer bb) {
            ringBuffer.publishEvent(TRANSLATOR, bb);
        }

    }

    /**
     * 事件都会有一个生成事件的源，这个例子中假设事件是由于磁盘 IO 或者 network 读取数据的时候触发的，事件源使用一个 ByteBuffer 来模拟它
     * 接受到的数据，也就是说，事件源会在 IO 读取到一部分数据的时候触发事件（触发事件不是自动的，程序猿需要在读取到数据的时候自己触发事件并发布）
     *
     * 由于需要事件预分配，它要求消息发布的两阶段方法，即声明环形缓冲区的插槽，然后发布数据，发布需要 try/catch
     * 如果声明了一个插槽，ringBuffer.next，就必须发布，否则会导致 Disruptor 状态损坏。
     * 建议使用 EventTranslator API
     */
    static class LongEventProducer {
        private final RingBuffer<LongEvent> ringBuffer;

        public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
            this.ringBuffer = ringBuffer;
        }

        /**
         * onData 用来发布事件，每调用一次就发布一次事件
         * 它的参数会通过事件传递给消费者
         *
         * @author   chengxudong               chengxudong@microwu.com
         * @date    2020/12/17  9:39
         *
         * @param   	bb
         * @return  void
         */
        public void onData(ByteBuffer bb) {
            // 可以把 ringBuffer 看成一个事件队列，那么 next 就是得到下一个时间槽
            long sequence = ringBuffer.next();

            try {
                // 用上面的索引取出一个空的事件用于填充
                LongEvent event = ringBuffer.get(sequence);
                event.value = bb.getLong(0);
            } finally {
                // 发布事件
                ringBuffer.publish(sequence);
            }
        }
    }

    /**
     * Java 8 API
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/25  15:21
     *
     * @param
     * @return  void
     */
    public static void test() throws InterruptedException {
        int bufferSize = 1024;

        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);

        disruptor.handleEventsWith(((event, l, b) -> System.out.println("Event: " + event)));

        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; l < 3; l++) {
            bb.putLong(0, l);
            ringBuffer.publishEvent((event, sequence, buffer) -> event.value = buffer.getLong(0), bb);
        }

        Thread.sleep(1000);
        disruptor.shutdown();

    }

    public static void main(String[] args) throws InterruptedException {
        // 事件工厂
        LongEventFactory factory = new LongEventFactory();

        // 指定环形缓冲区的大小，必须是 2 的倍数
        int bufferSize = 1024;

        // 构建一个 Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, DaemonThreadFactory.INSTANCE);

        // 连接到 handler
        disruptor.handleEventsWith(new LongEventHandler());

        // 开启 Disruptor，所有的线程开始运行
        disruptor.start();

        // 从 Disruptor 获取环形缓冲区用于发布
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventProducer producer = new LongEventProducer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            producer.onData(bb);
            Thread.sleep(1000);
        }

    }
}