package com.microwu.cxd.bean.component;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Description: BeanFactoryPostProcessor
 *  BeanFactoryPostProcessor 接口和 BeanPostProcessor 类似，都可以对 bean 的定义（配置元数据）进行处理
 *  IOC 容器允许 BeanFactoryPostProcessor 在容器实际实例化任何其他的 bean 之前读取配置元数据，并可能修改他
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/12   15:56
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("对容器进行后置处理。。。");
    }
}