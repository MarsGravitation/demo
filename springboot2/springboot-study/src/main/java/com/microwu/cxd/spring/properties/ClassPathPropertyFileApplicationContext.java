package com.microwu.cxd.spring.properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

import java.io.IOException;

/**
 * Description: 从properties 文件中读取bean definition
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   14:58
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClassPathPropertyFileApplicationContext extends AbstractRefreshableConfigApplicationContext{

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory defaultListableBeanFactory) throws BeansException, IOException {
        PropertiesBeanDefinitionReader beanDefinitionReader = new PropertiesBeanDefinitionReader(defaultListableBeanFactory);

        beanDefinitionReader.setEnvironment(this.getEnvironment());
        beanDefinitionReader.setResourceLoader(this);

        loadBeanDefinitions(beanDefinitionReader);
    }

    protected void loadBeanDefinitions(PropertiesBeanDefinitionReader reader) {
        String[] configResources = getConfigLocations();
        if (configResources != null) {
            reader.loadBeanDefinitions(configResources);
        }
    }

    public ClassPathPropertyFileApplicationContext(String configLocation) {
        this(new String[]{configLocation}, true, null);
    }

    public ClassPathPropertyFileApplicationContext(String[] configLocations, boolean refresh, ApplicationContext context) {
        super(context);

        setConfigLocations(configLocations);
        if (refresh) {
            refresh();
        }
    }
}