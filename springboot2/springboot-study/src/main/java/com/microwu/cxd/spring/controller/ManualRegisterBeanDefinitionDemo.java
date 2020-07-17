package com.microwu.cxd.spring.controller;

import com.microwu.cxd.spring.service.TestService;
import com.microwu.cxd.spring.service.impl.TestServiceImpl;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.Assert;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   10:12
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ManualRegisterBeanDefinitionDemo {

    /**
     * 通过构造器的方式来注入依赖
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/1  9:41
     *
     * @param
     * @return  void
     */
    private static void wireDependencyByConstructor() {
        // 1. 生成bean factory
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        // 2. 构造bean definition，并在 bean definition 中表达bean 之间的依赖
        GenericBeanDefinition serviceBeanDefinition = (GenericBeanDefinition) BeanDefinitionBuilder.genericBeanDefinition(TestServiceImpl.class).getBeanDefinition();
        System.out.println("TestServiceBeanDefinition: " + serviceBeanDefinition);

        GenericBeanDefinition controllerBeanDefinition = (GenericBeanDefinition) BeanDefinitionBuilder.genericBeanDefinition(ControllerByConstructor.class)
                // 这里是调用构造方法生成
//                .addConstructorArgReference("testServiceImpl")
//                .addConstructorArgValue("wire b constructor")
                // 这里是调用 property 相关方法
                .addPropertyReference("testService", "testServiceImpl")
                .addPropertyValue("name", "just test")
                .getBeanDefinition();

        // 3. 注册bean definition
        AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
        String beanNameForService = generator.generateBeanName(serviceBeanDefinition, factory);
        System.out.println("beanNameForService: " + beanNameForService);
        factory.registerBeanDefinition(beanNameForService, serviceBeanDefinition);

        String beanNameForController = generator.generateBeanName(controllerBeanDefinition, factory);
        System.out.println("beanNameForController: " + beanNameForController);
        factory.registerBeanDefinition(beanNameForController, controllerBeanDefinition);

        // 4. 获取bean
        ControllerByConstructor controller = factory.getBean(ControllerByConstructor.class);
        System.out.println("ControllerByConstructor: " + controller);

        TestService service = factory.getBean(TestServiceImpl.class);
        System.out.println("TestService: " + service);

        Assert.isTrue(controller.getTestService() == service);

    }

    public static void main(String[] args) {
        wireDependencyByConstructor();
    }
}