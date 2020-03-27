package com.microwu.cxd.blog;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   14:27
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ReturnConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        String exchange = "return_exchange";
        String queue = "return_queue";
        String routingKey = "return.#";
        // 声明 + 绑定
        channel.exchangeDeclare(exchange, "topic", true, false, null);
        channel.queueDeclare(queue, true, false, false, null);
        channel.queueBind(queue, exchange, routingKey);
        // 消费
        channel.basicConsume(queue, (DeliverCallback)(consumerTag, delivery) -> {
            System.out.println(new String(delivery.getBody()));
        }, (consumerTag) -> {});

    }
}