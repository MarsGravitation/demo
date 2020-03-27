package com.microwu.cxd.config;

import com.microwu.cxd.properties.RabbitmqProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.UUID;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   20:01
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
@EnableRabbit
public class RabbitmqConfig {
    @Autowired
    private RabbitmqProperties rabbitmqProperties;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitmqProperties.getHost());
        connectionFactory.setPort(rabbitmqProperties.getPort());
        connectionFactory.setUsername(rabbitmqProperties.getUsername());
        connectionFactory.setPassword(rabbitmqProperties.getPassword());
        connectionFactory.setPublisherReturns(true);
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        // 必须设置为true
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 一个 rabbitmq只能设置一次 confirm和return, 否则会报错
     *
     * @param
     * @return org.springframework.amqp.rabbit.core.RabbitTemplate
     * @author chengxudong               chengxudong@microwu.com
     * @date 2019/9/15  9:45
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routing key的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routing key分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    public TopicExchange exchange001() {
        // 持久化, 不自动删除
        return new TopicExchange("topic001_exchange", true, false);
    }

    @Bean
    public Queue queue001() {
        return new Queue("queue001", true,
                false, false, null);
    }

    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }

    @Bean
    public TopicExchange exchange002() {
        return new TopicExchange("topic002_exchange", true, false);
    }

    @Bean
    public Queue queue002() {
        return new Queue("queue002", true,
                false, false, null);
    }

    @Bean
    public Binding binding002() {
        return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbitmq.*");
    }

    @Bean
    public Queue queue003() {
        return new Queue("queue003", true,
                false, false, null);
    }

    @Bean
    public Binding binding003() {
        return BindingBuilder.bind(queue003()).to(exchange001()).with("mq.*");
    }

    /**
     * 用来消费消息
     *
     * @param connectionFactory
     * @return org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
     * @author chengxudong               chengxudong@microwu.com
     * @date 2019/9/15  9:33
     */
//    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        // 添加多个队列进行监听
        container.setQueues(queue001(), queue002(), queue003());
        // 当前消费者数量
        container.setConcurrentConsumers(1);
        // 最大消费者数量
        container.setMaxConcurrentConsumers(1);
        // 设置重回队列
        container.setDefaultRequeueRejected(false);
        // 设置ack 模式
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 设置listener外露
        container.setExposeListenerChannel(true);
        // 消费端标签生成策略
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String s) {
                return s + "_" + UUID.randomUUID().toString();
            }
        });
//         消息监听
//        container.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                channel.basicQos(0, 1, false);
////                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//                System.out.println("---------消费者:" + new String(message.getBody()));
//            }
//
//            @Override
//            public void onMessage(Message message) {
//                System.out.println(new String(message.getBody()));
//            }
//        });
//         自定义监听
//        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageDelegate());
//        messageListenerAdapter.setDefaultListenerMethod("consumerMessage");
////        messageListenerAdapter.setMessageConverter(new TextMessageConverter());
//        // 添加json转换器
////        messageListenerAdapter.setMessageConverter(new Jackson2JsonMessageConverter());
//        // 添加对Java对象的支持
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper defaultJackson2JavaTypeMapper = new DefaultJackson2JavaTypeMapper();
//        jackson2JsonMessageConverter.setJavaTypeMapper(defaultJackson2JavaTypeMapper);
//        messageListenerAdapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(messageListenerAdapter);

        return container;
    }

    /**
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/19  11:18
     *
     * @param   	
     * @return  org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
     */
    @Bean
    public SimpleRabbitListenerContainerFactory myRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMaxConcurrentConsumers(5);
        factory.setDefaultRequeueRejected(false);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
}