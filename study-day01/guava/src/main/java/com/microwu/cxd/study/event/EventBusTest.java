package com.microwu.cxd.study.event;

import com.google.common.eventbus.EventBus;

/**
 * https://blog.csdn.net/wuyuxing24/article/details/95505102
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/4/22  16:22
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class EventBusTest {

    public static void main(String[] args) {
        // 1. 创建一个 EventBus 对象
        EventBus eventBus = new EventBus("test");
        // 2. 注册监听者
        eventBus.register(new OrderEventListener());
        // 3. 发送消息
        OrderMessage message = new OrderMessage();
        message.setMessage("hello world!");
        eventBus.post(message);

    }

}
