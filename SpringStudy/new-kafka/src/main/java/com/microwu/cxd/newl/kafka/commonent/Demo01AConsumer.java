package com.microwu.cxd.newl.kafka.commonent;

import com.microwu.cxd.newl.kafka.entity.Demo01Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/20   16:43
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class Demo01AConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Demo01AConsumer.class);

    @KafkaListener(topics = Demo01Message.TOPIC, groupId = "demo01-A-consumer-group-" + Demo01Message.TOPIC)
    public void onMessage(ConsumerRecord<Integer, String> record) {
        LOGGER.info("[onMessage-A][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), record);
    }

}
