package com.microwu.cxd;

import com.microwu.cxd.config.ExpirationMessagePostProcessor;
import com.microwu.cxd.config.ProcessReceiver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/18   14:11
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringBootRabbitmqDelayQueueApplicationTests {
    /**
     * 发送到该队列的message会在一段时间后过期进入到delay_process_queue
     * 每个message可以控制自己的失效时间
     */
    final static String DELAY_QUEUE_PER_MESSAGE_TTL_NAME = "delay_queue_per_message_ttl";

    /**
     * 发送到该队列的message会在一段时间后过期进入到delay_process_queue
     * 队列里所有的message都有统一的失效时间
     */
    final static String DELAY_QUEUE_PER_QUEUE_TTL_NAME = "delay_queue_per_queue_ttl";
    final static int QUEUE_EXPIRATION = 4000;

    /**
     * message失效后进入的队列，也就是实际的消费队列
     */
    final static String DELAY_PROCESS_QUEUE_NAME = "delay_process_queue";

    /**
     * DLX
     */
    final static String DELAY_EXCHANGE_NAME = "delay_exchange";

    /**
     * 路由到delay_queue_per_queue_ttl的exchange
     */
    final static String PER_QUEUE_TTL_EXCHANGE_NAME = "per_queue_ttl_exchange";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 经过1秒, 2秒, 3秒收到消息
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/18  14:24
     *
     * @param
     * @return  void
     */
    @Test
    public void testDelayQueuePerMessageTTL() throws InterruptedException {
        ProcessReceiver.latch = new CountDownLatch(3);
        for (int i = 1; i <= 3; i++) {
            long expiration = i * 1000;
            rabbitTemplate.convertAndSend(DELAY_QUEUE_PER_MESSAGE_TTL_NAME,
                    (Object) ("Message From delay_queue_per_message_ttl with expiration " + expiration), new ExpirationMessagePostProcessor(expiration));
        }
        ProcessReceiver.latch.await();
    }

    /**
     * 过期队列, 一起收到消息
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/18  14:26
     *
     * @param   	
     * @return  void
     */
    @Test
    public void testDelayQueuePerQueueTTL() throws InterruptedException {
        ProcessReceiver.latch = new CountDownLatch(3);
        for (int i = 1; i <= 3; i++) {
            rabbitTemplate.convertAndSend(DELAY_QUEUE_PER_QUEUE_TTL_NAME,
                    "Message From delay_queue_per_queue_ttl with expiration " + QUEUE_EXPIRATION);
        }
        ProcessReceiver.latch.await();
    }

    /**
     * ProcessReceiver首先收到了3条会触发FAIL的消息，然后将其移动到缓冲队列之后，过了4秒，又收到了刚才的那3条消息。延迟重试场景测试成功。
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/18  14:27
     *
     * @param
     * @return  void
     */
    @Test
    public void testFailMessage() throws InterruptedException {
        ProcessReceiver.latch = new CountDownLatch(6);
        for (int i = 1; i <= 3; i++) {
            rabbitTemplate.convertAndSend(DELAY_PROCESS_QUEUE_NAME, ProcessReceiver.FAIL_MESSAGE);
        }
        ProcessReceiver.latch.await();
    }
}