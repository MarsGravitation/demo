package com.microwu.cxd.blog;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description: Exchange 接受消息, 并根据路由key转发到队列
 *  default exchange 是rabbitmq默认的交换机, 它属于 direct exchange,
 *  消息传递时, 需要routing key完全匹配才会被队列接受
 *
 *  Topic类型: 模糊匹配
 *  Fanout: 广播
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   12:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DirectProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取通道
        Channel channel = RabbitmqUtil.getChannel();
        // 发送消息
        channel.basicPublish("direct_exchange", "test002", null, "I am direct chang".getBytes());
        // 关闭
        channel.close();
    }
}