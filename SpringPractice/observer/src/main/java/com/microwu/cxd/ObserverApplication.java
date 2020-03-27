package com.microwu.cxd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 需求:
 *  1. 用户注册成功的时候, 根据用户提交的邮箱, 手机号, 向用户发送邮箱认证和手机号短信通知
 *      传统的做法之一是在我们的UserService注入邮件发送和短信发送的相关类, 然后调用对应类方法
 *      完成邮件发送和短信发送
 *
 *      这样做, 回把我们邮件, 短信发送业务与我们的UserService逻辑耦合. 如果要更改短信, 邮箱发送
 *      的API, 则需要更改UserService代码.
 *
 *      解决方法: Spring事件机制. 利用观察者设计模式, 设置监听器来监听注册事件(UserService - 事件发布者)
 *      三个主要对象: 事件发布者, 事件监听器, 事件源
 *
 */
@SpringBootApplication
public class ObserverApplication
{
    public static void main( String[] args )
    {

        SpringApplication.run(ObserverApplication.class, args);
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        UserService bean = context.getBean(UserService.class);
//        bean.doLogin("cxd@10102958989.qq.com", "恭喜您, 注册成功!!!");
    }
}
