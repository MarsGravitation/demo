package com.microwu.cxd.blog;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:     bind: 绑定exchange 和queue的关系, 参数有routing key
 *      Queue: 队列, 主要参数, 可持久化, 是否自动删除
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   12:51
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DirectConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        // 消费端声明交换机, 队列
        channel.exchangeDeclare("direct_exchange", "direct", true, false, false, null);
        channel.queueDeclare("direct_queue", false, false, false, null);
        // 绑定
        channel.queueBind("direct_queue", "direct_exchange", "test002");
        // 消费
        channel.basicConsume("direct_queue", (DeliverCallback)(consumerTag, delivery) -> {
            byte[] body = delivery.getBody();
            System.out.println(new String(body, "UTF-8"));
        }, consumerTag -> {});

    }
}