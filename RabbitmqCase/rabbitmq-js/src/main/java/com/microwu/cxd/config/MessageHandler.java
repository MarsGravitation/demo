package com.microwu.cxd.config;

import com.microwu.cxd.domain.Order;

import java.util.Map;

/**
 * Description:
 *
 * MessageListenerAdapter 和 MessageListener
 *  - 都是对消息队列监听处理, 但是优先级 MessageListener 高
 *
 *  MessageListenerAdapter 默认的处理方法名是handleMessage, 可以通过设置更改方法名
 *
 * 结论:
 *  1. 可以吧一个没有实现MessageListener 和 ChannelAwareMessageListener 接口适配成一个可以处理
 *      消息的处理器
 *  2. 默认方法名: handleMessage
 *  3. MessageListenerAdapter 支持不同的队列交给不同的方法去执行. 使用setQueueOrTagToMethodName方法设置
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/14   14:00
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MessageHandler {

    public void handleMessage(byte[] message){
        System.out.println("--------- byte handleMessage -------------");
        System.out.println(new String(message));
    }

    public void handleMessage(String message){
        System.out.println("--------- String handleMessage -------------");
        System.out.println(message);
    }

    /**
     * 生产者传递JSON类型, 消费者作为Map类型进行处理
     *
     * 总结:
     *  1. 使用Rabbitmq 的JSON处理器, 客户端发送JSON类型处理器,没有指定contentType 类型,
     *      Jackson 会将消息转换成byte[] 类型
     *  2. 如果指定了 application/json, 消费端转换成 map进行消费
     *  3. 如果是list的JSON格式, 消费端将转换成list消费
     *
     *  以上的没有什么意义, 最好能直接转换成对象
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/14  15:42
     *
     * @param   	message
     * @return  void
     */
    public void handleMessage(Map message){
        System.out.println("--------- Map handleMessage -------------");
        System.out.println(message);
    }

    /**
     * 在消息header中, 添加一个TypeId属性, value 为类的全路径名, 这样可以在消费端转换成对象
     * 但是这样的耦合度太高
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/14  15:52
     *
     * @param   	order
     * @return  void
     */
    public void handleMessage(Order order){
        System.out.println("--------- Order handleMessage -------------");
        System.out.println(order);
    }

    public void onMessage(byte[] message) {
        System.out.println("---------onMessage--------");
        System.out.println(new String(message));
    }

    public void stringMessage(String message) {
        System.out.println("========= String Message =========");
        System.out.println(message);
    }
}