package com.micruwo.cxd.rocketmq.simple;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * Description:
 *
 * 1. 使用 RocketMQ 通过三种方式发送消息：可靠同步、可靠异步，单向传输
 * 2. 使用 RocketMQ 消费消息
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/19   13:51
 * Update History:
 * Author        Time            Content
 */
public class SyncProducer {

    /**
     * 可靠同步传输应用场景广泛，如重要通知消息、短信通知、短信营销系统等。
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/7/19 14:01
     *
     * @param: args - [java.lang.String]
     * @return: void
     */
    public static void main(String[] args) throws Exception {
        // Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer("please_rename_unique_group_name");
        // Specify name server addresses.
        producer.setNamesrvAddr("192.168.139.129:9876");
        producer.setSendMsgTimeout(5000);
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 100; i++) {
            // Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " +
                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            // Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }
        // Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }
}
