package com.microwu.cxd.spring.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/9   9:53
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@ComponentScan(basePackages = {"com.microwu.cxd"})
@Configuration(proxyBeanMethods = false)
public class SpringConfiguration {

    @Bean
    public CustomBeanDefinitionRegistry customBeanDefinitionRegistry() {
        return new CustomBeanDefinitionRegistry();
    }

    @Bean
    public HelloBeanPostProcessor beanPostProcessor() {
        return new HelloBeanPostProcessor();
    }

    @Bean
    public HelloBeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new HelloBeanFactoryPostProcessor();
    }

}