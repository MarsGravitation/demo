package com.microwu.cxd.listener;

import com.microwu.cxd.event.SendEmailEvent;
import com.microwu.cxd.event.SendMessageEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Description:    事件监听器需要实现ApplicationListener接口
 *
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/3   22:35
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
@Async
public class RegisterLisneter implements ApplicationListener {
    /**
     * 当事件发布者发布消息后, 监听者监听到就会执行该方法
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/3  22:39
     *
     * @param   	applicationEvent
     * @return  void
     */
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof SendEmailEvent) {
            System.out.println("正在发送邮件......");
        } else if(applicationEvent instanceof SendMessageEvent) {
            System.out.println("发送短信......");
        }

    }
}