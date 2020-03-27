package com.microwu.cxd.event;

import org.springframework.context.ApplicationEvent;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/3   22:41
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SendMessageEvent extends ApplicationEvent {
    private String phoneNum;

    public SendMessageEvent(Object source, String phoneNum) {
        super(source);
        this.phoneNum = phoneNum;
    }
}