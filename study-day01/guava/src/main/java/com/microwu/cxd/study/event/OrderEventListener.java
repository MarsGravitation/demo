package com.microwu.cxd.study.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * 监听类
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/4/22  16:25
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class OrderEventListener {

    /**
     * 如果发送了 orderMessage，会由这个方法进行处理
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/4/22     16:29
     *
     * @param orderMessage
     * @return void
     */
    @Subscribe
    public void dealWithEvent(OrderMessage orderMessage) {
        System.out.println("处理消息：" + orderMessage.getMessage());
    }

}
