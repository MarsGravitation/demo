package com.microwu.cxd.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/17   15:36
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootApplication
public class ProviderApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ProviderApplication.class, args);
        String name = context.getEnvironment().getProperty("user.name");
        String age = context.getEnvironment().getProperty("user.age");
        System.out.println(name + " : " + age);
    }
}