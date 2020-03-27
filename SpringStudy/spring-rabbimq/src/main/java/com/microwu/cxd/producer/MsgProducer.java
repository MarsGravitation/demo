package com.microwu.cxd.producer;

import com.microwu.cxd.config.RabbitmqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/31   17:34
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class MsgProducer implements RabbitTemplate.ConfirmCallback {
    private static final Logger logger = LoggerFactory.getLogger(MsgProducer.class);

    // 因为被设置为原型, 所以不能自动注入
    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/7/31  17:36
     *
     * @param   	rabbitTemplate
     * @return
     */
    @Autowired
    public MsgProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        //
        rabbitTemplate.setConfirmCallback(this);

    }

      public void sendMsg(String content) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 把消息放入ROUTINGKEY_A对应的队列当中去, 对应的队列A
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_A, RabbitmqConfig.ROUTINGKEY_A, content, correlationData);
    }

    /**
     * 回调
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/7/31  17:39
     *
     * @param   	correlationData
     * @param 		ack
     * @param 		cause
     * @return  void
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info("回调id:" + correlationData);
        if(ack) {
            logger.info("消息消费成功");
        } else {
            logger.info("消息消费失败" + cause);
        }

    }
}