package com.microwu.cxd.blog;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description: return 消息机制主要是用来匹配一些无法路由的消息
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   14:20
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ReturnProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        String exchange = "return_exchange";
        String routingKey = "error";
        // 设置 mandatory = true, 如果为false, Broker会自动删除消息
        channel.basicPublish(exchange, routingKey, true, null, "this is return message".getBytes());
        channel.addReturnListener((int replyCode, String replyText, String returnExchange,
                                   String returnRoutingKey, AMQP.BasicProperties properties, byte[] body) -> {
            System.out.println(replyCode);
            System.out.println(replyText);
            System.out.println(returnExchange);
            System.out.println(returnRoutingKey);
            System.out.println(new String(body));
        });


    }
}