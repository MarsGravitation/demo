package com.microwu.cxd.blog;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   14:59
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DlxProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        String exchange = "test_exchange";
        String routingKey = "dlx.save";

        // 过期队列设置属性
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2).contentEncoding("UTF-8").expiration("10000").build();
        for(int i = 0; i < 5; i++) {
            channel.basicPublish(exchange, routingKey, true, properties, "this is dlx message".getBytes());
        }
    }
}