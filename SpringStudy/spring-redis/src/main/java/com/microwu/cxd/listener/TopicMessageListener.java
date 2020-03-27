package com.microwu.cxd.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/6/22   21:59
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class TopicMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();// 请使用valueSerializer
        byte[] channel = message.getChannel();
        //设置监听频道
        String topic = new String(channel);
        //key
        String itemValue = new String(body);
        System.out.println("频道topic:"+topic);
        System.out.println("过期的键值对的K:"+itemValue);
    }
}