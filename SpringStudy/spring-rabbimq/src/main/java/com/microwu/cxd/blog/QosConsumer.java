package com.microwu.cxd.blog;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   14:42
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class QosConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        String exchange = "qos_exchange";
        String queue = "qos_queue";
        String routingKey = "qos.#";

        channel.exchangeDeclare(exchange, "topic");
        channel.queueDeclare(queue, true, false, false, null);
        channel.queueBind(queue, exchange, routingKey);

        // 限流, 表示只接受一条消息, 如果不给Broker 应答, 就不会有消息继续接受
        channel.basicQos(0, 1, false);
        // false 表示 需要手动 ack
        channel.basicConsume(queue, false, new MyConsumer(channel));

    }
}