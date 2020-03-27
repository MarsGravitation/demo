package com.microwu.cxd.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microwu.cxd.domain.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   20:20
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitmqTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test01() {
        // 创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc", "消息头信息描述");
        messageProperties.getHeaders().put("type", "自定义类型");
        // 消息
        Message message = new Message("Hello Rabbitmq".getBytes(), messageProperties);
        // 发送
        rabbitTemplate.convertAndSend("topic001_exchange", "spring.test", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("添加额外的配置");
                // 会覆盖以前的配置
                message.getMessageProperties().getHeaders().put("desc", "额外配置");
                message.getMessageProperties().getHeaders().put("attr", "额外属性");
                return message;
            }
        });

    }

    @Test
    public void test02() throws JsonProcessingException {
        Order order = new Order(1L, "订单01", "外星人台式机");
        // 转换为json
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(order);
        System.out.println(json);

        MessageProperties messageProperties = new MessageProperties();
        // 一定要设置
        messageProperties.setContentType("application/json");
        // 添加typeId
        messageProperties.getHeaders().put("__TypeId__", "com.microwu.cxd.domain.Order");
        Message message = new Message(json.getBytes(), messageProperties);
        // 发送消息
        rabbitTemplate.convertAndSend("topic001_exchange", "spring.save", message);
    }

    @Autowired
    private RabbitSender rabbitSender;

    @Test
    public void test03() throws InterruptedException {
        for(int i = 0; i < 10; i++) {
            rabbitSender.send("我叫成旭东", null);
        }
        Thread.sleep(100000);
    }
}