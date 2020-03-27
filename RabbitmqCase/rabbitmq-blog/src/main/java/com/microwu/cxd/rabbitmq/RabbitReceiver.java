package com.microwu.cxd.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/15   10:01
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class RabbitReceiver {

    @RabbitListener(containerFactory = "myRabbitListenerContainerFactory",
            bindings = @QueueBinding(
            value = @Queue(value = "queue001",
                    durable="true"),
            exchange = @Exchange(value = "topic001_exchange",
                    durable="true",
                    type= "topic",
                    ignoreDeclarationExceptions = "true"),
            key = "spring.*"
    )
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        System.err.println("--------------------------------------");
        System.err.println("消费端Payload: " + message.getPayload());
        Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        //手工ACK,获取deliveryTag true表示确认所有消息, false 表示确认当前消息
        channel.basicQos(0, 1, false);
        // 确认模式代表 如果消费者正在监听, 但是没有确认, 就是 uncheck状态
        // 如果消费者停掉服务, 则会变成ready状态
//        channel.basicAck(deliveryTag, false);
    }
}