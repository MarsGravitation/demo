package com.microwu.cxd.newl.kafka.commonent;

import com.microwu.cxd.newl.kafka.entity.Demo01Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/20   13:09
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class Demo01Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Demo01Consumer.class);

    /**
     * 在方法上，添加了 @KafkaListener 注解，声明消费的 Topic 是
     * "Demo_01"，消费者分组是 "demo01-consumer-group-DEMO_01"。一
     * 般情况下，我们建议一个消费者分组，仅消费一个 Topic。这样做会有一
     * 个好处：每个消费者分组职责单一，只消费一个 Topic
     *
     * 方法参数，使用消费 Topic 对应的消息类即可。
     *
     * 虽然说，@KafkaListener 注解是方法级别的，还是建议一个类，对应一个
     * 方法，消费消息，简单清晰
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2022/5/20 9:12
     *
     * @param: message - [com.microwu.cxd.kafka.entity.Demo01Message]
     * @return: void
     */
    @KafkaListener(topics = Demo01Message.TOPIC, groupId = "demo01-consumer-group-" + Demo01Message.TOPIC)
    public void onMessage(Demo01Message message) {
        LOGGER.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread(), message);
    }

}
