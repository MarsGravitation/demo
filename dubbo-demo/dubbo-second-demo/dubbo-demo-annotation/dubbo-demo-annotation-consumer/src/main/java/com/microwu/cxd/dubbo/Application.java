package com.microwu.cxd.dubbo;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.microwu.cxd.dubbo.comp.DemoServiceComponent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/4   15:40
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();
        DemoServiceComponent demoServiceComponent = context.getBean("demoServiceComponent", DemoServiceComponent.class);

        String world = demoServiceComponent.sayHello("world");
        System.out.println(world);
    }

    @Configuration
    @EnableDubbo(scanBasePackages = "com.microwu.cxd.dubbo.comp")
    @PropertySource("classpath:/spring/dubbo-consumer.properties")
    @ComponentScan(value = {"com.microwu.cxd.dubbo.comp"})
    static class ConsumerConfiguration {

    }
}