package com.microwu.cxd.config;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Description:
 *      1. Broker: 它提供了一种传输服务, 他的角色就是维护一条从生产者到消费者的路线,
 *          保证数据能按照指定的方式进行传输
 *      2. Exchange: 消息交换机, 它指定消息按什么规则, 路由到那个队列
 *      3. Queue: 消息的载体, 每个消息都会被投到一个或多个队列
 *      4. Binding: 绑定, 他的作用就是吧exchange 和 queue按照路由规则绑定起来
 *      5. RoutingKey: 路由关键字, exchange根据这个关键字进行消息投递
 *      6. vhost: 虚拟主机, 一个broker里可以有多个vhost, 用作不同用户的权限分离
 *      7. Producer: 消息生产者, 就是投递消息的程序
 *      8. Consumer: 消息消费者, 就是接受消息的程序
 *      9. Channel: 消息通道, 在客户端的每个连接里, 可建立多个channel
 *
 *      https://blog.csdn.net/qq_38455201/article/details/80308771
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/31   17:05
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
public class RabbitmqConfig {
    private static final Logger logger = LoggerFactory.getLogger(RabbitmqConfig.class);

    public static final String EXCHANGE_A = "my-mq-exchange_A";
    public static final String EXCHANGE_B = "my-mq-exchange_B";

    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_B = "QUEUE_B";

    public static final String ROUTINGKEY_A = "spring-boot-routingKey_A";
    public static final String ROUTINGKEY_B = "spring-boot-routingKey_B";

    @Autowired
    private RabbitmqProperties rabbitmqProperties;

    @Bean
    public ConnectionFactory connectinFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitmqProperties.getHost(), rabbitmqProperties.getPort());
        connectionFactory.setUsername(rabbitmqProperties.getUsername());
        connectionFactory.setPassword(rabbitmqProperties.getPassword());
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;

    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectinFactory());
        return rabbitTemplate;
    }

    /**
     * 针对消费者消费
     *  1. 设置交换机类型
     *  2. 将队列绑定到交换机
     *
     *  FanoutExchange: 将消息分发到所有的绑定队列, 无routingkey概念
     *  HeadersExchange: 通过添加属性key-value匹配
     *  DirectExchange: 按照routingKey分发到指定队列
     *  TopicExchange: 多关键字匹配
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/7/31  17:43
     *
     * @param
     * @return  org.springframework.amqp.core.DirectExchange
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_A);
    }

    /**
     * 获取队列A
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/7/31  17:46
     *
     * @param
     * @return  org.springframework.amqp.core.Queue
     */
    @Bean
    public Queue queueA() {
        // 队列持久化
        return new Queue(QUEUE_A, true);
    }

    /**
     * 一个交换机可以绑定多个消息队列, 也就是消息通过一个交换机, 可以分发到不同的队列当中去
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/7/31  17:46
     *
     * @param
     * @return  Binding
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queueA()).to(directExchange()).with(RabbitmqConfig.ROUTINGKEY_A);

    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        // 加载处理消息A的队列
        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer(connectinFactory());
        // 设置接受多个队列里面的消息, 这里面设置接受队列A
        messageListenerContainer.setQueues(queueA());
        messageListenerContainer.setExposeListenerChannel(true);
        // 设置最大的并发的消费者数量
        messageListenerContainer.setMaxConcurrentConsumers(10);
        // 最小的并发消费者数量
        messageListenerContainer.setConcurrentConsumers(1);
        // 设置确认模式手工确认
        messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        messageListenerContainer.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                channel.basicQos(1);
                byte[] body = message.getBody();
                logger.info("接受处理队列A当中的消息:" + new String(body, "UTF-8") );
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        });
        return messageListenerContainer;

    }


}