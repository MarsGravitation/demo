package com.microwu.cxd;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author   chengxudong               chengxudong@microwu.com
 * @date    2019/11/14  14:48
 *
 */
@EnableRabbit
@SpringBootApplication
public class RabbitmqJsApplication {
    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RabbitmqJsApplication.class);

        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);
        System.out.println(rabbitTemplate);

//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.getHeaders().put("desc", "消息发送");
//        messageProperties.getHeaders().put("type", 10);
//
//        Message message = new Message("hello".getBytes(), messageProperties);

        /**
         * 调用rabbitTemplate的send方法发送消息，如果没有指定exchange，Routing，则使用声明Exchange指定的
         * exchange，Routing
         * 如果RabbitTemplate没有设置，则默认的exchange 是DEFAULT_EXCHANGE为""，
         * 默认的route key是DEFAULT_ROUTING_KEY为""
         */
        //1.
        //rabbitTemplate.send(message);

        //2.指定Routing key,而exchange是Rabbitmq默认指定的
        //rabbitTemplate.send("zhihao.error",message);

        //3.即指定exchange，又指定了routing_key
        //rabbitTemplate.send("zhihao.login","ulogin",message);

        /**
         * 4.使用默认的defaultExchange进行投递消息，route key就是队列名，指定correlation_id属性，correlation_id属性是rabbitmq 进行异步rpc进行标识每次请求的唯一
         * id,下面会讲到
         */
//        rabbitTemplate.send("", "cxd.first.queue", message, new CorrelationData("spring.amqp"));

        /**
         * 使用converAndSend方法, 接受的参数是 Object, 其实是将接收的对象转换成message对象,
         *
         * rabbitTemplate.convertAndSend("this is my message");
         * rabbitTemplate.convertAndSend("zhihao.error","this is my message order111");
         *
         * 发送消息的后置处理器, MessagePostProcessor 类的psotProcessMessage 方法得到的Message 就是将参数
         * Object 转换成 Message
         *
         *  rabbitTemplate.convertAndSend("", "zhihao.user.queue", "this is my message processor", new MessagePostProcessor() {
         *             //在后置处理器上加上order和count属性
         *             @Override
         *             public Message postProcessMessage(Message message) throws AmqpException {
         *                 System.out.println("-------处理前message-------------");
         *                 System.out.println(message);
         *                 message.getMessageProperties().getHeaders().put("order",10);
         *                 message.getMessageProperties().getHeaders().put("count",1);
         *                 return message;
         *             }
         *         });
         *
         *          rabbitTemplate.convertAndSend("", "cxd.first.queue", "message before", message -> {
         *             MessageProperties messageProperties = new MessageProperties();
         *             messageProperties.getHeaders().put("desc", "消息发送");
         *             messageProperties.getHeaders().put("type", 10);
         *
         *             Message message1 = new Message("message afteer".getBytes(), messageProperties);
         *             return message1;
         *         });
         *
         */

//        Order order = new Order(1, 1, 88d, "123");
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(order);
//
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setContentType("application/json");
//        // 指定的_TypeId_ 属性值必须是消费端的全类名
//        messageProperties.getHeaders().put("__TypeId__", "order");
//        Message message = new Message(json.getBytes(), messageProperties);
//        rabbitTemplate.convertAndSend("", "cxd.first.queue", message);

//        Order order = new Order(1, 1, 88d, "123");
//        rabbitTemplate.convertAndSend("", "cxd.first.queue", order.toString().getBytes(), new CorrelationData(order.getId().toString()));

//        byte[] body = "hello, world!".getBytes();
//
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setContentType("json");
//        // 消息设置过期时间
//        // 过期的消息被删除
//        // 可以设置消息过期, 或者队列过期, 如果同时设置, 时间短的先生效
//        messageProperties.setExpiration("30000");
//
//        Message message = new Message(body, messageProperties);
//        rabbitTemplate.convertAndSend("", "cxd.first.queue", message);

        // 死信队列 - 在队列上指定一个Exchange
        // 1. 消息被拒绝 - reject or nack, 且 requeue = false
        // 2. 消息过期而被删除
        // 3. 消息数量超过队列最大限制而被删除
        // 3. 消息总大小超过队列最大限制而被删除


        // 就会把该消息转发到指定的这个exchange, 同时也可一直顶一个x-dead-leter-routing-key
        // r如果没有指定, 则使用消息的routing-key(也和exchange有关, 如果是Fanout, 将会转发到 所有的Queue)
        // 实验失败 - 后续ttl + dle 再测试

        // 优先级队列 - 不能保证严格的顺讯消费, 所以我认为不太重要, 略过, 知道有这么一个特性

        // RPC 通信, 稍后在学

//        byte[] body = "hello, world!".getBytes();
//
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setContentType("json");
//
//        Message message = new Message(body, messageProperties);
//
//        rabbitTemplate.convertAndSend("cxd.first.exchange", "a", message);

        TimeUnit.SECONDS.sleep(30);

        context.close();
    }
}
