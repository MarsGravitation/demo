package com.microwu.cxd.service;

import com.microwu.cxd.kafka.KafkaApplication;
import com.microwu.cxd.kafka.component.Demo01Producer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/20   9:42
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KafkaApplication.class)
public class Demo01ProducerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Demo01ProducerTest.class);

    @Autowired
    private Demo01Producer producer;

    @Test
    public void testSyncSend() throws ExecutionException, InterruptedException {
        int id = (int) (System.currentTimeMillis() / 1000);
        SendResult result = producer.sendResult(id);
        LOGGER.info("[testSyncSend][发送编号：[{}] 发送结果：[{}]]", id, result);

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

    @Test
    public void testAsyncSend() throws InterruptedException {
        int id = (int) (System.currentTimeMillis() / 1000);
        producer.asyncSend(id).addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.info("[testASyncSend][发送编号：[{}] 发送异常]]", id, throwable);
            }

            @Override
            public void onSuccess(SendResult<Object, Object> objectObjectSendResult) {
                LOGGER.info("[testASyncSend][发送编号：[{}] 发送成功，结果为：[{}]]", id, objectObjectSendResult);
            }

        });

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

}
