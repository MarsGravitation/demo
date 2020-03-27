package com.microwu.cxd.publish;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/1   15:17
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ReceiveLogs {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 临时队列
        String queue = channel.queueDeclare().getQueue();
        // 交换机和队列的绑定, 告诉交换机将消息发送到那个队列
        // 第三个参数: routingKey
        channel.queueBind(queue, EXCHANGE_NAME, "");

        System.out.println("等待消息, 退出按CTRL + C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received " + message);
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {});

    }
}