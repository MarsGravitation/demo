package com.microwu.cxd.spring;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description: spring 启动时，是怎么读取XML 文件的
 *      1. 给 ClassPathXmlApplicationContext 设置 XML 文件的路径
 *      2. refresh 内部的 beanFactory，其实这个时候 BeanFactory 都还没有创建，会先创建 DefaultListableBeanFactory
 *      3. ClassPathXmlApplicationContext 调用其 loadBeanDefinitions， 将新建 DefaultListableBeanFactory 作为参数传入
 *      4. ClassPathXmlApplicationContext 内部会持有一个 XmlBeanDefinitionReader，且 XmlBeanDefinitionReader 内部是持有之前创建的
 *          DefaultListableBeanFactory 的，这个时候就简单了， XmlBeanDefinitionReader 负责读取 XML，将 bean definition 解析出来，
 *          丢给 DefaultListableBeanFactory，此时，XmlBeanDefinitionReader 就完成了
 *       5. DefaultListableBeanFactory 里面还没有业务bean，只有一堆的bean definition，后面ClassPathXmlApplicationContext 直接去实例化需要在启动时实例化的 bean
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/2   10:53
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class XmlBootStrap {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("util-namespace-test-constant.xml");

        BeanDefinition beanDefinition = context.getBeanFactory().getBeanDefinition("chin.age");
        System.out.println(beanDefinition);
        System.out.println(JSON.toJSONString(beanDefinition));
    }
}