package com.micruwo.cxd.rocketmq.simple;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/19   17:07
 * Update History:
 * Author        Time            Content
 */
public class OnewayProducer {

    /**
     * 单向传输用于需要中等可靠的情况，例如日志收集
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/7/19 17:07
     *
     * @param: args - [java.lang.String]
     * @return: void
     */
    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        producer.setNamesrvAddr("192.168.139.129:9876");
        producer.start();
        for (int i = 0; i < 100; i++) {
            Message msg = new Message("TopicTest",
                    "TagA",
                    ("Hello RocketMQ" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.sendOneway(msg);
        }
        Thread.sleep(5000);
        producer.shutdown();
    }
}
