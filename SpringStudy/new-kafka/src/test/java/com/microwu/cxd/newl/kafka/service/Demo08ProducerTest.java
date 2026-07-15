package com.microwu.cxd.newl.kafka.service;

import com.microwu.cxd.newl.kafka.NewKafkaApplication;
import com.microwu.cxd.newl.kafka.commonent.Demo08Producer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/24   14:49
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewKafkaApplication.class)
public class Demo08ProducerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Demo08Producer producer;

    @Test
    public void testSyncSend() throws ExecutionException, InterruptedException {
        for (int id = 1; id <= 2; id++) {
            SendResult result = producer.syncSend(id);
            logger.info("[testSyncSend][发送编号：[{}] 发送结果：[{}]]", id, result);
        }

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

}