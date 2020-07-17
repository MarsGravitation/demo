package com.microwu.cxd.spring.json;

import com.microwu.cxd.spring.controller.ControllerByConstructor;
import com.microwu.cxd.spring.service.impl.TestServiceImpl;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   11:27
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BootStrap {
    public static void main(String[] args) {
        ClassPathJsonApplicationContext context = new ClassPathJsonApplicationContext("beanDefinition.json");
        ControllerByConstructor controller = context.getBean(ControllerByConstructor.class);
        TestServiceImpl service = context.getBean(TestServiceImpl.class);
        assert controller.getTestService() == service;
        System.out.println(controller);
        System.out.println(service);
    }
}