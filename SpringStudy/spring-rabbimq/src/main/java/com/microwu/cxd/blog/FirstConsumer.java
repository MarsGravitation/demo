package com.microwu.cxd.blog;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 *  消费者不能关闭通道
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   9:56
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class FirstConsumer {
    public static final String QUEUE_NAME = "test001";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取通道
        Channel channel = RabbitmqUtil.getChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 消费消息
        System.out.println("准备消费消息...");
        channel.basicConsume(QUEUE_NAME, true, (DeliverCallback)(consumerTag, delivery) -> {
            byte[] body = delivery.getBody();
            System.out.println(new String(body, "UTF-8"));
        }, consumerTag -> {});

    }
}