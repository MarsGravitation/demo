package com.microwu.cxd.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * Description:
 *      消息类型转换
 *
 * MessageListenerAdapter 内部通过MessageConverter 把Message 转换成Java对象, 然后找到相应的
 * 处理方法
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/14   14:32
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TextMessageConverter implements MessageConverter {
    /**
     * 将Java对象转换成Message对象
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/14  14:33
     *
     * @param   	o
     * @param 		messageProperties
     * @return  org.springframework.amqp.core.Message
     */
    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        System.out.println("============ toMessage ===========");
        return new Message(o.toString().getBytes(), messageProperties);
    }

    /**
     * 将Message对象转换成 Java对象
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/14  14:33
     *
     * @param   	message
     * @return  java.lang.Object
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.out.println("=============== from message ==============");
        return new String(message.getBody());
    }
}