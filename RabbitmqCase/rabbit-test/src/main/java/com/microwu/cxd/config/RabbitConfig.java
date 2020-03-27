package com.microwu.cxd.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/8   9:30
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@EnableRabbit
@Configuration
public class RabbitConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setVirtualHost("local_rabbit_hosts");
        connectionFactory.setHost("192.168.133.134");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("cxd");
        connectionFactory.setPassword("123456");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    DirectExchange dspSmsTtlExchange() {
        return new DirectExchange("dsp_delay_exchange", true, false);
    }

    @Bean
    Queue delayQueue() {
        return QueueBuilder.durable("dsp_delay_queue")
//                .withArgument("x-dead-letter-exchange", DSP_SMS_EXCHANGE)
//                .withArgument("x-dead-letter-routing-key", DSP_SMS_KEY)
                .withArgument("x-message-ttl", 30000)
                .build();
    }

}