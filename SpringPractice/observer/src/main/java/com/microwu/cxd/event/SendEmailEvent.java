package com.microwu.cxd.event;

import org.springframework.context.ApplicationEvent;

/**
 * Description:    定义事件, 扩展抽象类 ApplicationEvent, 同时将事件源作为构造函数参数
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/3   22:30
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SendEmailEvent extends ApplicationEvent {
    private String emailAddress;
    /**
     * 定义事件的核心对象: 发送目的地, 供监听器调用完成邮箱发送功能
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/3  22:32
     *
     * @param   	source
     * @return
     */
    public SendEmailEvent(Object source, String emailAddress) {
        // source 事件发布者
        super(source);
        this.emailAddress = emailAddress;
    }
}