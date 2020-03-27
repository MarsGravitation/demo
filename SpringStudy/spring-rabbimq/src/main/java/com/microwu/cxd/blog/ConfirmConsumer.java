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
 * Date:       2019/9/14   13:21
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConfirmConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        String exchangeName = "confirm_exchange";
        String routingKey = "test003";
        String queueName = "confirm_queue";

        //3 声明交换机和队列 然后进行绑定设置, 最后制定路由Key
        channel.exchangeDeclare(exchangeName, "direct", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        // 发送消息
        channel.basicConsume(queueName, (DeliverCallback)(consumerTag, delivery) -> {
            byte[] body = delivery.getBody();
            System.out.println(new String(body, "UTF-8"));
        }, consumerTag -> {});

    }
}