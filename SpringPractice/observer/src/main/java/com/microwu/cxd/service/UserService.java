package com.microwu.cxd.service;

import com.microwu.cxd.event.SendEmailEvent;
import com.microwu.cxd.event.SendMessageEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * Description:    定义事件发布者
 *      事件发送的代表类是 ApplicationEventPublisher, 我们的事件发布类常实现ApplicationEventPublisherAware接口,
 *      同时需要定义成员属性ApplicationEvent来发布我们的事件
 *
 *      除了实现ApplicationEventPublisherAware, 我们还可以实现ApplicationContextAware接口,
 *      它继承了ApplicationEventPublisherAware. ApplicationContext是我们事件容器的上层, 我们发布事件,
 *      也可以通过此容器完成
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/3   22:44
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class UserService implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 注入事件发布者
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/3  22:54
     *
     * @param   	applicationEventPublisher
     * @return  void
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void doLogin(String email, String message) {
        System.out.println("========开始注册======");
        SendEmailEvent sendEmailEvent = new SendEmailEvent(this, email);
        SendMessageEvent sendMessageEvent = new SendMessageEvent(this, message);
        // 发布事件
        this.applicationEventPublisher.publishEvent(sendEmailEvent);
        this.applicationEventPublisher.publishEvent(sendMessageEvent);
    }

}