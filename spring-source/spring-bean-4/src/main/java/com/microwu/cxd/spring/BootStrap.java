package com.microwu.cxd.spring;

import com.microwu.cxd.spring.context.ClassPathJsonApplicationContext;
import com.microwu.cxd.spring.controller.TestController;
import com.microwu.cxd.spring.service.TestService;

/**
 * Description: 自定义 ApplicationContext，从 json 文件中读取bean definition?
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   17:31
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BootStrap {
    public static void main(String[] args) {
        // 创建自定义 json 上下文
        ClassPathJsonApplicationContext context = new ClassPathJsonApplicationContext("beanDefinition.json");

        TestController controller = context.getBean(TestController.class);
        TestService service = context.getBean(TestService.class);
        System.out.println(controller + "\r\n" + service);
    }
}