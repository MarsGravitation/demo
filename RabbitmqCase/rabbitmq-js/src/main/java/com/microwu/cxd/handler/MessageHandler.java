package com.microwu.cxd.handler;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/14   17:11
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class MessageHandler {

    /**
     * 如果消息content_type = text, 则处理方法的参数必须是String类型
     *
     * 如果参数是 Message, 都不会报错
     *
     * 步骤:
     *  1. 启动类加@EnableRabbit注解
     *  2. 注入RabbitListenerContainerFactory的bean
     *  3. 写一个消息处理类托管给spring容器, 方法上增加@RabbitmqListener注解
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/14  17:16
     *
     * @param
     * @return  void
     */
//    @RabbitListener(queues = "cxd.first.queue")
//    public void handleMessage(byte[] message) {
//        System.out.println("消费消息");
//        System.out.println(new String(message));
//    }

    /**
     * 如果不指定content_type, body 显示的是字节数组
     *
     * 1. 可以向多个队列消费消息
     * 2. 进行声明
     *  > 支持自动声明绑定, 声明后自动监听队列, binding 不能和 queue同时指定
     *  > @RabbitListener 注在类上面, 需要配合@RabbitHandler使用
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/14  17:24
     *
     * @param   	body
     * @param 		headers
     * @return  void
     */
//    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "cxd.first.queue"), exchange = @Exchange(value = "cxd.exchange"), key = "hello")})
//    public void handleMessage(@Payload String body, @Headers Map<String, Object> headers) {
//        System.out.println("消费消息");
//        System.out.println(body);
//        System.out.println(headers);
//    }

//    @RabbitListener(queues = "cxd.first.queue")
//    public void handleMessage(Order order) {
//        System.out.println("====消费对象===" + order);
//    }

    @RabbitListener(queues = "cxd.first.queue")
    public void handleMessage(byte[] bytes, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        System.out.println("======= 拒绝消息 ====");
        System.out.println(new String(bytes));
        // 消费未确认显示的是 未确认 = 0, ready = total
//        channel.basicNack(tag, false, false);
        // 拒绝消息 - false, 删除消息, 只是语义上失败; true, 重入队列
        channel.basicReject(tag, false);
        // 确认消息 - true/false 没有什么区别
//        channel.basicAck(tag, true);
    }
}