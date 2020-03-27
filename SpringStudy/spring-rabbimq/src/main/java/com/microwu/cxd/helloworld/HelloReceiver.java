package com.microwu.cxd.helloworld;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/1   14:17
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HelloReceiver {
    private static final String QUEUE_NAME = "hello";

    /**
     * 打开一个连接和一个通道, 声明我们要消耗的队列, 需要与发送的队列匹配
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/1  14:26
     *
     * @param   	args
     * @return  void
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println("等待消息, 退出按CTRL + C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received " + message);

        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }

}