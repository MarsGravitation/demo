package com.microwu.cxd.queue.disruptor.app;

/**
 *
 * Disruptor 应用
 *
 *  1. disruptor 应该如何用才能发挥最大功效？
 *   disruptor 原本就是事件驱动的设计。将 disruptor 作为业务处理，中间带 I/O 处理，
 *   比多线程还慢；相反，如果将 disruptor 做业务处理，需要 I/O 时采用 nio 异步调用，
 *   不阻塞 disruptor 消费线程，等待 I/O 异步调用回来后在回调方法中将后续处理重新塞到
 *   disruptor 队列中，可以让 disruptor 发挥最大功效
 *
 *  2. 如果 buffer 常常是满的怎么办？
 *   一种是把 buffer 变大
 *   一种是从源头解决 producer 和 consumer 速度差异太大问题，比如试着把 producer
 *   分流，或者用多个 disruptor，使每个 disruptor 的 load 变小
 *
 *  3. 什么时候使用 disruptor？
 *   如果对延迟的需求很高，可以考虑使用
 *
 * https://blog.csdn.net/zhouzhenyong/article/details/81303011
 *
 * 线程模型
 *
 * https://www.javadoop.com/post/apm
 *
 * Spring 整合 Disruptor
 *
 * https://github.com/anair-it/disruptor-spring-manager
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/7/10  15:34
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class DisruptorApplication {

    public static void main(String[] args) {

    }

}
