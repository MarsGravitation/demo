package com.microwu.cxd.newl.kafka.commonent;

import com.microwu.cxd.newl.kafka.entity.Demo05Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/23   9:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class Demo05Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Demo05Consumer.class);

    @KafkaListener(topics = Demo05Message.TOPIC, groupId = "demo05-consumer-group-" + Demo05Message.TOPIC + "-" + "#{T(java.util.UUID).randomUUID()}")
    public void onMessage(Demo05Message message) {
        LOGGER.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

}
