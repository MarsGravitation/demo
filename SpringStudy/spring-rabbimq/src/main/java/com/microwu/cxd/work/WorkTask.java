package com.microwu.cxd.work;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/1   14:44
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class WorkTask {
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();

        // durable 持久化: true
        // exclusive 独占: false
        // autoDelete 自动删除: false
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        int i = 1;
        String message = "send " + ++i + " message";
        // 将消息标记为持久化 MessageProperties.PERSISTENT_TEXT_PLAIN
        channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
        System.out.println("Send message ...");

    }
}