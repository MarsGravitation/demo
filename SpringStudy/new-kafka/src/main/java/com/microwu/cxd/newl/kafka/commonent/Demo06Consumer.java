package com.microwu.cxd.newl.kafka.commonent;

import com.microwu.cxd.newl.kafka.entity.Demo06Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/24   8:48
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class Demo06Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Demo06Consumer.class);

    @KafkaListener(topics = Demo06Message.TOPIC,
            groupId = "demo06-consumer-group-" + Demo06Message.TOPIC,
            concurrency = "2")
    public void onMessage(Demo06Message message) {
        LOGGER.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

}
