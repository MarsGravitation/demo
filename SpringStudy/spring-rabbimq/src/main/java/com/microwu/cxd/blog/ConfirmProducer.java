package com.microwu.cxd.blog;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description: 确认机制: 当生产者发送消息给Broker, Broker 会返回结果
 * 步骤: channel 开启确认模式
 *      添加监听
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   13:16
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConfirmProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        //3 指定我们的消息投递模式: 消息的确认模式
        channel.confirmSelect();

        String exchangeName = "confirm_exchange";
        String routingKey = "test003";

        //4 发送一条消息
        String msg = "Hello RabbitMQ Send confirm message!";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
        // 开启监听
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.err.println("-------no ack!-----------");
            }

            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.err.println("-------ack!-----------");
            }
        });
//        channel.close();

    }
}