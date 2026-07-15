package com.microwu.cxd.kafka.component;

import com.microwu.cxd.kafka.entity.Demo01Message;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/19   17:45
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class Demo01Producer {

    @Resource
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public SendResult sendResult(Integer id) throws ExecutionException, InterruptedException {
        Demo01Message message = new Demo01Message(id);
        // 同步发送消息
        return kafkaTemplate.send(Demo01Message.TOPIC, message).get();
    }

    public ListenableFuture<SendResult<Object, Object>> asyncSend(Integer id) {
        Demo01Message message = new Demo01Message(id);
        // 异步发送消息
        return kafkaTemplate.send(Demo01Message.TOPIC, message);
    }

}
