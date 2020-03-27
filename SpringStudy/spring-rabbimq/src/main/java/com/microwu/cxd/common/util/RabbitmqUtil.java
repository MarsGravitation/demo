package com.microwu.cxd.common.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/1   14:38
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RabbitmqUtil {

    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.133.134");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("cxd");
        connectionFactory.setPassword("123456");
        // 创建抽象套接字连接, 为我们负责协议版本和省份验证
        Connection connection = connectionFactory.newConnection();
        // 创建Channel
        Channel channel = connection.createChannel();

        return channel;
    }
}