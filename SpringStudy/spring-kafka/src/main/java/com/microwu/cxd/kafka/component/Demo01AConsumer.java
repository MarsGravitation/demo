package com.microwu.cxd.kafka.component;

import com.microwu.cxd.kafka.entity.Demo01Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/20   9:16
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class Demo01AConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Demo01AConsumer.class);

    /**
     * 差异一，topic 一样，消费者分组修改成了 "demo01-A-consumer-group-DEMO_01".
     * 这样，我们就可以出师 Kafka 集群消费的特性。
     *
     * 集群消费：集群消费模式下，相同 Consumer Group 的每个 Consumer 实例平均
     * 分摊消息。
     *  - 也就是说，如果我们发送一条 Topic 的消息，可以分别被两个 group 都消费一次
     *  - 但是，如果我们启动两个该示例的实例，则消费者分组 demo01 和 demo01-A 都会
     *  有多个 Consumer 实例。此时我们再发送一条消息，只会被 demo01-A 的一个 Consumer
     *  消费一次，也同样只会被 demo01-A 的一个 Consumer 消费一次
     *
     *  总结：同一个 group 的消费者只会消费一次，不同的 group 可以多次消费
     *
     * 差异二：方法参数，ConsumerRecord，我们可以获取倒消费消息的信息，例如说
     * 消息的所属队列、创建时间等属性，不过消息的内容需要自己去反序列化。
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2022/5/20 9:32
     *
     * @param: record - [org.apache.kafka.clients.consumer.ConsumerRecord<java.lang.Integer]
     * @return: void
     */
    @KafkaListener(topics = Demo01Message.TOPIC, groupId = "demo01-A-consumer-group-" + Demo01Message.TOPIC)
    public void onMessage(ConsumerRecord<Integer, String> record) {
        LOGGER.info("[onMessage][thread:{}, message: {}]", Thread.currentThread(), record);
    }

}
