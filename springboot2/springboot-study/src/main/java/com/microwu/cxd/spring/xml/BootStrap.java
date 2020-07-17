package com.microwu.cxd.spring.xml;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description: Spring 启动时，是怎么读取XML文件的
 *  1. 给 ClassPathXmlApplicationContext 设置 XML 文件的路径
 *  2. refresh 内部的 beanFactory，其实这个时候 BeanFactory 还没创建，会先创建 DefaultListableBeanFactory
 *  3. ClassPathXmlApplicationContent 会调用其 loadBeanDefinitions， 将新建 DefaultListableBeanFactory 作为参数传入
 *  4. ClassPathXmlApplicationContext 内部会持有一个 XmlBeanDefinitionReader，且 XmlBeanDefinitionReader 内部是持有之前
 *      创建的 DefaultListableBeanFactory，此时，XmlBeanDefinitionReader 负责读取 xml，将 bean definition 解析出来，
 *      丢给DefaultListableBeanFactory
 *   5. DefaultListableBeanFactory 里面没有业务 bean，只有一堆 bean definition，后面 ClassPathXmlApplication 直接实例化
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   15:18
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BootStrap {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("");
        context.start();

//        AbstractRefreshableApplicationContext
    }
}