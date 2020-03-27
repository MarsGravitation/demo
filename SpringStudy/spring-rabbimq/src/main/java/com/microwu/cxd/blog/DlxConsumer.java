package com.microwu.cxd.blog;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   15:07
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DlxConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        String exchange = "test_exchange";
        String queue = "test_queue";
        String routKey = "dlx.#";

        channel.exchangeDeclare(exchange, "topic", true, false, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", "dlx_exchange");
        // 这个属性要声明到队列上, 死信属性
        channel.queueDeclare(queue, true, false, false, map);
        channel.queueBind(queue, exchange, routKey);

        // 死信队列
        channel.exchangeDeclare("dlx_exchange", "topic", true, false, null);
        channel.queueDeclare("dlx_queue", true, false, false, null);
        channel.queueBind("dlx_queue", "dlx_exchange", "#");

        channel.basicConsume(queue, true, new MyConsumer(channel));


    }
}