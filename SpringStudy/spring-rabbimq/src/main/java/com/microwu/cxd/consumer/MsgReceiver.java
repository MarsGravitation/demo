package com.microwu.cxd.consumer;

import com.microwu.cxd.config.RabbitmqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/31   17:49
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
@RabbitListener(queues = RabbitmqConfig.QUEUE_A)
public class MsgReceiver {
    private static final Logger logger = LoggerFactory.getLogger(MsgReceiver.class);

    @RabbitHandler
    public void process(String content) {
        logger.info("接受处理队列A中的消息" + content);
    }
}