package com.microwu.cxd.newl.kafka.commonent;

import com.microwu.cxd.newl.kafka.entity.Demo08Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/24   14:47
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class Demo08Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Demo08Consumer.class);

    @KafkaListener(topics = Demo08Message.TOPIC,
            groupId = "demo08-consumer-group-" + Demo08Message.TOPIC)
    public void onMessage(Demo08Message message, Acknowledgment acknowledgment) {
        LOGGER.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
        // 提交消费进度
        if (message.getId() % 2 == 1) {
            acknowledgment.acknowledge();
        }
    }

}
