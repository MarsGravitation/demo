package com.microwu.cxd.spring;

import com.microwu.cxd.spring.controller.TestController;
import com.microwu.cxd.spring.service.TestService;
import com.microwu.cxd.spring.service.impl.TestServiceImpl;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * Description: 通过代码方式手动生成bean definition 并注册到 bean Factory
 *  1. 选择bean definition 实现类，并实例化bean definition
 *  2. 注册bean definition
 *  3. get bean 查看是否 work
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   17:13
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BootStrap {
    public static void main(String[] args) {
        // 1. 生成bean factory
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        // 2. 构造bean definition，并在 bean definition 中表达 bean 之间的依赖关系
        GenericBeanDefinition serviceBeanDefinition = (GenericBeanDefinition) BeanDefinitionBuilder.genericBeanDefinition(TestServiceImpl.class).getBeanDefinition();
        GenericBeanDefinition controllerBeanDefinition = (GenericBeanDefinition) BeanDefinitionBuilder
                .genericBeanDefinition(TestController.class)
                // 这里是构造函数
                .addConstructorArgReference("testServiceImpl")
                .addConstructorArgValue("wire by constructor")
                // 这里是调用的property相关方法
//                .addPropertyReference("t","testService")
//                .addPropertyValue("name","just test")
                .getBeanDefinition();

        // 3. 注册 bean definition
        AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
        String serviceBeanName = generator.generateBeanName(serviceBeanDefinition, factory);
        factory.registerBeanDefinition(serviceBeanName, serviceBeanDefinition);

        String controllerBeanName = generator.generateBeanName(controllerBeanDefinition, factory);
        factory.registerBeanDefinition(controllerBeanName, controllerBeanDefinition);

        // 4. 获取 bean
        TestController testController = factory.getBean(TestController.class);
        // 思考：这里为什么 TestService.class 可以?
        TestService testService = factory.getBean(TestService.class);
        System.out.println(testController + "\r\n" + testService);
    }
}