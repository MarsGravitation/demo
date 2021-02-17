package com.microwu.cxd.queue.disruptor;

import com.lmax.disruptor.*;

import java.util.concurrent.*;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/17   15:40
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class EventProcessorMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int bufferSize = 1024;
        int threadNumbers = 4;

        // 创建一个单生产者的 RingBuffer
        // 第一个参数是 EventFactory，从名字上理解是事件工厂，其实它的职责就是产生数据填充 RingBuffer 的区块
        // 第二个参数是 RingBuffer 的大小，必须是 2 的指数倍，目的是为了讲求模运算转换为 & 运算提高效率
        // 第三个参数是 RingBuffer 的生产在都没有可用区块的时候（可能是消费者（或者是事件处理器）太慢了）的等待策略
        final RingBuffer<Trade> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<Trade>() {
            @Override
            public Trade newInstance() {
                return new Trade();
            }
        }, bufferSize, new YieldingWaitStrategy());
        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadNumbers);
        // 创建 SequenceBarrier
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        // 创建消息处理器
        BatchEventProcessor<Trade> tradeBatchEventProcessor = new BatchEventProcessor<>(ringBuffer, sequenceBarrier, new TradeHandler());
        // 这一步的目的是把消费者的位置信息引用注入到生产者 如果只有一个消费者的情况可以省略
        ringBuffer.addGatingSequences(tradeBatchEventProcessor.getSequence());
        // 把消息处理器提交到线程池
        executorService.submit(tradeBatchEventProcessor);
        // 如果存在多个消费者，那么重复执行上面三行代码，把 TradeHandler 换成其他消费者类
        Future<Trade> future = executorService.submit(new Callable<Trade>() {
            @Override
            public Trade call() throws Exception {
                long seq;
                for (int i = 0; i < 10; i++) {
                    // 占一个坑，ringBuffer 一个可用区块
                    seq = ringBuffer.next();
                    // 给这个区块放入数据
                    ringBuffer.get(seq).setPrice(Math.random() * 9999);
                    // 发布这个区块的数据使 handler 可见
                    ringBuffer.publish(seq);
                }
                return null;
            }
        });

        // 等待生产者结束
        future.get();
        // 等待一秒钟，等消费者处理完成
        Thread.sleep(1000);
        // 通知时间处理器，可以结束了，并不是马上结束
        tradeBatchEventProcessor.halt();
        // 终止线程
        executorService.shutdown();
    }

}