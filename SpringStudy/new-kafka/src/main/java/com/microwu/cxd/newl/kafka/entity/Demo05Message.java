package com.microwu.cxd.newl.kafka.entity;

/**
 * Description:
 *
 * 广播消费
 *
 * 广播消费模式下，相同 Consumer Group 的每个 Consumer 实例都接收全量的消息。
 *
 * 不过 Kafka 并不直接提供内置的广播消费的功能！！！此时，我们只能退而求其次，
 * 每个 Consumer 独有一个 Consumer Group，从而保证都能接收到全量的消息。
 *
 * spring.kafka.consumer.auto-offset-reset=latest
 * 因为在广播订阅下，我们一般情况下，无需消费历史的消息，而是从订阅的 Topic 的
 * 队列的尾部开始消费即可。
 *
 * 通过 Spring EL 表达式，在每个消费者分组的名字上，使用 UUID 生成其后缀。这样，
 * 我们就能保证每个项目启动的消费者分组不同，以达到广播消费的目的。
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/23   9:31
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Demo05Message {

    public static final String TOPIC = "DEMO_05";

}
