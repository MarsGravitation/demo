package com.microwu.cxd.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/4   14:53
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Application {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/dubbo-consumer.xml");
        context.start();

        DemoService demoService = context.getBean("demoService", DemoService.class);
        String world = demoService.sayHello("world");
        System.out.println(world);
    }
}