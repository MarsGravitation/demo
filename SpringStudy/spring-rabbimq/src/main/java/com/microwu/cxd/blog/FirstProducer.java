package com.microwu.cxd.blog;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   9:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class FirstProducer {
    public static final String QUEUE_NAME = "test001";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取通道
        Channel channel = RabbitmqUtil.getChannel();
        System.out.println("发送消息...");
        // 发送消息, 参数, "" - 默认交换机: 根据routing key 进行路由, routing key, null, 消息
        channel.basicPublish("", QUEUE_NAME, null, "Hello world".getBytes());
        // 关闭channel
        channel.close();

    }
}