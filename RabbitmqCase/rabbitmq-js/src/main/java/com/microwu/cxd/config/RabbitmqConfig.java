package com.microwu.cxd.config;

import com.microwu.cxd.domain.Order;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/14   11:01
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
public class RabbitmqConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("192.168.133.134");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("cxd");
        connectionFactory.setPassword("123456");
        connectionFactory.setPublisherReturns(true);
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    /**
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/15  9:09
     *
     * @param   	connectionFactory
     * @return  org.springframework.amqp.rabbit.core.RabbitAdmin
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 忽略声明异常 - 主要是队列同名, 但是属性不一样, 会导致声明失败
        rabbitAdmin.setIgnoreDeclarationExceptions(true);
        return rabbitAdmin;
    }

    /**
     * ReturnListener:
     *  如果 mandatory = true, 当消息不能路由到队列中去的时候, 会触发return method
     *  如果 false, 当消息不能路由到队列中的时候, server 会删除该消息
     *
     *  总结: ReturnListener
     *      1. 设置 factory.setPublisherReturns(true)
     *      2. rabbitmqTemplate.setMandatory(true)
     *      3. 无法路由的消息会触发 setReturnCallback方法
     *
     * ConfirmCallback:
     *  生产者与broker 之间的消息可靠性保证的思路:
     *      1. 当消息发送到broker的时候, 会执行监听的回调函数, 其中deliveryTag是消息id
     *      2. 在生产端要维护一个消息发送的表, 消息发送的时候记录消息id, 在消息成功落地
     *      broker 磁盘并且进行回调确认ack 的时候, 根据本地消息表和回调确认的消息id进行对比,
     *      这样可以确保生产端的消息表中的没有进行回调确认的消息进行补救式重发, 当然不可避免的就会在消息
     *      端造成消息重复消息. 针对消费端重复消息, 在消费端进行幂等处理
     *
     * 步骤:
     *      1. ConnectionFactory.setPusherConfirms(true)
     *      2. rabbitTemplate.setConfirmCallback()
     *      3. 发消息的时候, 需要制定CorrelationData, 用于标识发送的唯一id
     *      4. 但是不支持批量确认
     *
     * Consumer ack:
     *  broker 与消费者之间的消息确认称为 consumer acknowledgements, 用于解决消费者与
     *  Rabbitmq服务器之间消息可靠传输, 它是在消费端消费成功后通知broker 删除消息
     *
     *  Publisher Confirms:
     *      1. 否定确认和重新入队
     *      当Rabbitmq 无法成功处理消息时, 它会返回生产端nack. 在这种情况下, nack和ack意义相同, 且requeue是没意义的
     *      是否重入队列由发送者决定
     *
     *      当通道进入publisher confirms 模式, 所有的消息只能被confirmed 或nack一次
     *      nack 只有erlang 进程处理队列时发生内部错误才会被发送
     *
     *      2. 消息确认的时机
     *          > mandatory = false, 不可路由的消息, 发送ack 或 nack
     *          > mandatory = true, 先发送return, 在确认消息(发送ack/nack)
     *      对于可路由的消息, 需要满足:
     *          > 消息被路由到所有的队列中
     *          > 需要持久化的消息持久化到磁盘中
     *          > 镜像队列同步到所有队列
     *
     *      3. 持久化消息的确认延迟
     *          为了保持持久化效率, 它是定时批量持久化消息, 延迟可能比较大
     *          为了提高吞吐量, 建议异步处理或者批量发送后等到未完成的确认
     *
     * 我的理解:
     *  nack 只有erlang发送内部错误才会返回, 这时由发送者决定是否重发
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/15  9:08
     *
     * @param   	connectionFactory
     * @return  org.springframework.amqp.rabbit.core.RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey) -> {
            System.out.println("=========== returnMessage ===========");
            System.out.println("replyCode:" + replyCode);
            System.out.println("replyText:" + replyText);
            System.out.println("exchange:" + exchange);
            System.out.println("routingKey:" + routingKey);

        });
//        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
//            if(ack) {
//                System.out.println("============ 消息id " + correlationData + " 被成功发送 ============");
//            }else {
//                System.out.println("============ 消息id " + correlationData + " 被失败发送 ============");
//            }
//        });
        return rabbitTemplate;
    }

    /**
     * MessageListener - 消费消息
     *      当一个queue上有多个消费者时, 只会有一个消费者收到消息, 一般是多个消费者轮流收到消息
     *
     *      增加后置处理器, 接收到的消息都添加header 请求头 - container.setAfterReceivePostProcessors
     *
     * 消息监听适配器 - adapter
     *      通过反射将消息处理委托给目标监听器的处理方法, 并进行灵活的消息类型转换.
     *      允许监听器方法对消息内容进行操作, 完全独立于 Rabbit API
     *
     *      默认情况下, 传入 Rabbit消息的内容在被传递到目标监听器方法之前被提取, 以使目标
     *      方法对消息内容类型(String 或者 byte)进行操作, 而不是原始Message 类型
     *
     *      消息类型转换委托给 MessageConverter 接口的实现类. 默认情况下, 使用SimpleMessageConverter
     *
     *      如果目标监听器方法返回一个非空对象, 通常是消息内容类型, String 或者byte数组, 它将被包装在
     *      一个Rabbit Message中, 并发送使用来自Rabbit ReplyTo属性通过 setResponseToutingKey
     *      指定的routingKey的routingKey来传送消息 - 使用rabbitmq来实现异步rpc 功能时会使用这个属性
     *
     *      注意: 发送响应体消息仅在使用ChannelAwareMessageListener 入口点 - 通常通过Spring 消息监听器容器
     *      时可用. 用作MessageListener 不支持生成响应消息
     *
     * Converter
     *  1. 生产者发送的是list json 数据, 需要增加一个__ContentTypeId__ 用于指明List 里面的具体对象
     *  2. 如果是map json数据, 则需要指定__KeyTypeId__, __ContentTypeId__ 的header, 用于指明map里面的key, value 具体对象
     *
     *  消费端ACK
     *      MANUAL: 手动确认, 需要实现ack, nack, 使用ChannleAwareMessageListener 监听
     *  注意:
     *      ack: 肯定确认
     *      nack: 否定确认
     *      reject: 否定确认
     *
     *      ack 表示已成功传递消息, 可以丢弃. reject 同样丢弃消息, 只是未成功处理
     *
     *      这里涉及requeue - 重入, nack 和 reject 一个区别, nack 接受一个附加参数, 可以拒绝或重新排队多个消息
     *
     *      建议跟踪重新交付次数并永久拒绝消息, 或延迟后重新安排重新排队
     *
     *      stackoverflow建议: https://stackoverflow.com/questions/23158310/how-do-i-set-a-number-of-retry-attempts-in-rabbitmq
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/14  11:50
     *
     * @param   	connectionFactory
     * @return  org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
     */
    int count = 0;
//    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 监听多个queue
        container.setQueueNames("cxd.first.queue");
        // 设置 消费者的 consumerTag
        container.setConsumerTagStrategy(queue -> "cxd" + (count++));
        // 设置消费者的Arguments
        HashMap<String, Object> map = new HashMap<>();
        map.put("module", "订单模块");
        map.put("fun", "发送消息");
        container.setConsumerArguments(map);
        // 设置消费者的并发数量
        container.setConcurrentConsumers(5);
        container.setMaxConcurrentConsumers(10);
        //
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 设置处理器的消费消息的默认方法
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageHandler());
//        messageListenerAdapter.setDefaultListenerMethod("stringMessage");
//        messageListenerAdapter.setMessageConverter(new TextMessageConverter());

        // 指定Json转换器, 自带的json转换器, 但是没有指定消息的contentType
        // 消费端使用byte 来消费, 说明将其转换成byte[]

        //消费端配置映射
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();

        HashMap<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("order", Order.class);

        DefaultJackson2JavaTypeMapper defaultJackson2JavaTypeMapper = new DefaultJackson2JavaTypeMapper();
        defaultJackson2JavaTypeMapper.setIdClassMapping(idClassMapping);

        jackson2JsonMessageConverter.setJavaTypeMapper(defaultJackson2JavaTypeMapper);
        messageListenerAdapter.setMessageConverter(jackson2JsonMessageConverter);

        TextMessageConverter textMessageConverter = new TextMessageConverter();

        ContentTypeDelegatingMessageConverter contentTypeDelegatingMessageConverter = new ContentTypeDelegatingMessageConverter();
        contentTypeDelegatingMessageConverter.addDelegate("text",textMessageConverter);
        contentTypeDelegatingMessageConverter.addDelegate("html/text",textMessageConverter);
        contentTypeDelegatingMessageConverter.addDelegate("xml/text",textMessageConverter);
        contentTypeDelegatingMessageConverter.addDelegate("text/plain",textMessageConverter);

        contentTypeDelegatingMessageConverter.addDelegate("json",jackson2JsonMessageConverter);
        contentTypeDelegatingMessageConverter.addDelegate("application/json",jackson2JsonMessageConverter);

//        contentTypeDelegatingMessageConverter.addDelegate("image/jpg",new JPGMessageConverter());
//        contentTypeDelegatingMessageConverter.addDelegate("image/jepg",new JPGMessageConverter());
//        contentTypeDelegatingMessageConverter.addDelegate("image/png",new JPGMessageConverter());
        // ContentTypeDelegatingMessageConverter 是一个代理的MessageConverter
        // 本身不做消息转换的具体动作, 而是将消息转换委托给具体的MessageConverter, 我们可以设置ContentType 和Converter的映射关系
        // 还有一个默认的MessageConverter
        messageListenerAdapter.setMessageConverter(contentTypeDelegatingMessageConverter);

//        container.setMessageListener(messageListenerAdapter);
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            System.out.println("======== 消费消息 =======");
//            if(message.getMessageProperties().getHeaders().get("error") == null) {
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//                System.out.println("消息确认");
//            }else {
//                // false 代表手动ack, 如果消费者正在监听这个队列, 消息进入unacked, 如果消费者停掉, 消息变成ready
//                // 只有ack确认之后, 才会在Server中删除
//                // nack 第二个参: false 手动确认, 第三个参数: 重回队列 true
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//                System.out.println("消息拒绝");
//            }
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        });
//        container.setMessageListener((message) -> {
//            // 具体的消费逻辑
//            System.out.println("====== 消费者1 =====");
//            System.out.println("======== 消息体 " + new String(message.getBody()) + " ======");
//        });
        return container;
    }

    /**
     * 在这里设置消息转化器
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/14  17:20
     *
     * @param   	connectionFactory
     * @return  org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory
     */
    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        // 发现消息中有content_type = text, 默认转换成String
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        simpleRabbitListenerContainerFactory.setConnectionFactory(connectionFactory);
        // 可靠消息总结:
        // 1. 持久化, exchange, queue, message
        // 2.消息确认: 生产端, 消费端
        // 3. 对于重要消息, 要结合本地消息表
        // AUTO: 1. 如果消息被成功消费, 消费过程中没有抛出异常, 自动确认
        //      2. 抛出ImmediateAcknowledgeAmqpException, 消费者会被确认
        //      3. 抛出AmqpRejectAndDontRequeueException, 消息被拒绝, 且 requeue = false, 不重入队列
        //      4. 其他异常, 消息被拒绝, 且requeue = true, 重入队列, 单消费者会死循环, 对消费端也会造成资源浪费,setDefaultRequeueRejected = true
        simpleRabbitListenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 这个是和AUTO搭配使用, 避免重入队列
//        simpleRabbitListenerContainerFactory.setDefaultRequeueRejected(true);
//        simpleRabbitListenerContainerFactory.setMessageConverter(new MessageConverter() {
//            @Override
//            public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
//                return null;
//            }
//
//            @Override
//            public Object fromMessage(Message message) throws MessageConversionException {
//                return new Order(1, 1, 22L, new String(message.getBody()));
//            }
//        });
        return simpleRabbitListenerContainerFactory;
    }

//    @Bean
    public RabbitListenerConfigurer rabbitListenerConfigurer() {
        return (registrar) -> {
            // endpoint 设置队列的消息处理逻辑
            SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
            endpoint.setId("10");
            endpoint.setQueueNames("cxd.first.queue");
            endpoint.setMessageListener((message -> {
                System.out.println("endpoint处理消息逻辑");
                System.out.println(new String(message.getBody()));
            }));

            // 使用适配器来处理消息
//            SimpleRabbitListenerEndpoint endpoint2 = new SimpleRabbitListenerEndpoint();
//            endpoint2.setId("12");
//            endpoint2.setQueueNames("order", "pay");
//            System.out.println("endpoint2处理消息逻辑");
//            endpoint2.setMessageListener(new MessageListenerAdapter(new MessageHandler()));

            // 注册两个endpoint
            registrar.registerEndpoint(endpoint);
//            registrar.registerEndpoint(endpoint2);
        };
    }

    /**
     * 设置队列的过期时间
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/18  9:44
     *
     * @param
     * @return  org.springframework.amqp.core.Queue
     */
//    @Bean
    public Queue queue() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("x-message-ttl", 30000);
        return new Queue("ttl.queue", true, false, false, map);
    }

    /**
     * 加一个额外的特性: 队列长度限制
     *  设置一个队列只有5条消息, 当第六条消息发送过来, 会删除最早的那条消息
     *
     *  UI界面设置: queue.arguments {x-max-length-bates: 10}
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/18  9:49
     *
     * @param
     * @return  org.springframework.amqp.core.Queue
     */
//    @Bean
    public Queue queueLimit() {
        HashMap<String, Object> arguments = new HashMap<>();
        // 设置队列中最多存放三条消息
        // 控制队列中的消息数量
        arguments.put("x-max-length", 3);
        // 控制队列中的消息总的大小
        arguments.put("x-max-length-bytes", 10);
        return new Queue("length.limit.queue", true, false, false, arguments);
    }

//    @Bean
//    public DirectExchange ttlExchange() {
//        return new DirectExchange("ttl_exchange");
//    }
//
//    @Bean
//    public DirectExchange dleExchange() {
//        return new DirectExchange("dle");
//    }
//
//    @Bean
//    public Queue ttlQueue() {
//        return QueueBuilder.durable("ttl_queue")
//                .withArgument("x-dead-letter-exchange", "dle")
//                .withArgument("x-dead-letter-routing-key", "ttl")
//                .build();
//    }
//
//    @Bean
//    public Queue dleQueue() {
//        return QueueBuilder.durable("dle_queue").build();
//    }
//
//    @Bean
//    public Binding ttlBinding(Queue ttlQueue, DirectExchange ttlExchange) {
//        return BindingBuilder.bind(ttlQueue)
//                .to(ttlExchange)
//                .with("ttl");
//    }
//
//    @Bean
//    public Binding dleBinding(Queue dleQueue, DirectExchange dleExchange) {
//        return BindingBuilder.bind(dleQueue)
//                .to(dleExchange)
//                .with("ttl");
//    }
//    /**
//     * 发送到该队列的message会在一段时间后过期进入到delay_process_queue
//     * 每个message可以控制自己的失效时间
//     */
//    final static String DELAY_QUEUE_PER_MESSAGE_TTL_NAME = "delay_queue_per_message_ttl";
//
//    /**
//     * 发送到该队列的message会在一段时间后过期进入到delay_process_queue
//     * 队列里所有的message都有统一的失效时间
//     */
//    final static String DELAY_QUEUE_PER_QUEUE_TTL_NAME = "delay_queue_per_queue_ttl";
//    final static int QUEUE_EXPIRATION = 4000;
//
//    /**
//     * message失效后进入的队列，也就是实际的消费队列
//     */
//    final static String DELAY_PROCESS_QUEUE_NAME = "delay_process_queue";
//
//    /**
//     * DLX
//     */
//    final static String DELAY_EXCHANGE_NAME = "delay_exchange";
//
//    /**
//     * 路由到delay_queue_per_queue_ttl的exchange
//     */
//    final static String PER_QUEUE_TTL_EXCHANGE_NAME = "per_queue_ttl_exchange";
//
//    /**
//     * 创建DLX exchange
//     *
//     * @return
//     */
//    @Bean
//    DirectExchange delayExchange() {
//        return new DirectExchange(DELAY_EXCHANGE_NAME);
//    }
//
//    /**
//     * 创建per_queue_ttl_exchange
//     *
//     * @return
//     */
//    @Bean
//    DirectExchange perQueueTTLExchange() {
//        return new DirectExchange(PER_QUEUE_TTL_EXCHANGE_NAME);
//    }
//
//    /**
//     * 创建delay_queue_per_message_ttl队列
//     *
//     * @return
//     */
//    @Bean
//    Queue delayQueuePerMessageTTL() {
//        return QueueBuilder.durable(DELAY_QUEUE_PER_MESSAGE_TTL_NAME)
//                .withArgument("x-dead-letter-exchange", DELAY_EXCHANGE_NAME) // DLX，dead letter发送到的exchange
//                .withArgument("x-dead-letter-routing-key", DELAY_PROCESS_QUEUE_NAME) // dead letter携带的routing key
//                .build();
//    }
//
//    /**
//     * 创建delay_queue_per_queue_ttl队列
//     *
//     * @return
//     */
//    @Bean
//    Queue delayQueuePerQueueTTL() {
//        return QueueBuilder.durable(DELAY_QUEUE_PER_QUEUE_TTL_NAME)
//                .withArgument("x-dead-letter-exchange", DELAY_EXCHANGE_NAME) // DLX
//                .withArgument("x-dead-letter-routing-key", DELAY_PROCESS_QUEUE_NAME) // dead letter携带的routing key
//                .withArgument("x-message-ttl", QUEUE_EXPIRATION) // 设置队列的过期时间
//                .build();
//    }
//
//    /**
//     * 创建delay_process_queue队列，也就是实际消费队列
//     *
//     * @return
//     */
//    @Bean
//    Queue delayProcessQueue() {
//        return QueueBuilder.durable(DELAY_PROCESS_QUEUE_NAME)
//                .build();
//    }
//
//    /**
//     * 将DLX绑定到实际消费队列
//     *
//     * @param delayProcessQueue
//     * @param delayExchange
//     * @return
//     */
//    @Bean
//    Binding dlxBinding(Queue delayProcessQueue, DirectExchange delayExchange) {
//        return BindingBuilder.bind(delayProcessQueue)
//                .to(delayExchange)
//                .with(DELAY_PROCESS_QUEUE_NAME);
//    }
//
//    /**
//     * 将per_queue_ttl_exchange绑定到delay_queue_per_queue_ttl队列
//     *
//     * @param delayQueuePerQueueTTL
//     * @param perQueueTTLExchange
//     * @return
//     */
//    @Bean
//    Binding queueTTLBinding(Queue delayQueuePerQueueTTL, DirectExchange perQueueTTLExchange) {
//        return BindingBuilder.bind(delayQueuePerQueueTTL)
//                .to(perQueueTTLExchange)
//                .with(DELAY_QUEUE_PER_QUEUE_TTL_NAME);
//    }
//
//    @Bean
//    SimpleMessageListenerContainer processContainer(ConnectionFactory connectionFactory, ProcessReceiver processReceiver) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(DELAY_PROCESS_QUEUE_NAME); // 监听delay_process_queue
//        container.setMessageListener(new MessageListenerAdapter(processReceiver));
//        return container;
//    }

}