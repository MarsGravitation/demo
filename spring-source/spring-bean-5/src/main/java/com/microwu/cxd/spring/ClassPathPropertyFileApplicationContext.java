package com.microwu.cxd.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/2   10:14
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClassPathPropertyFileApplicationContext extends AbstractRefreshableConfigApplicationContext {
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        PropertiesBeanDefinitionReader beanDefinitionReader = new PropertiesBeanDefinitionReader(beanFactory);

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

    public ClassPathPropertyFileApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent) {
        super(parent);

        setConfigLocations(configLocations);
        if (refresh) {
            refresh();
        }
    }
}