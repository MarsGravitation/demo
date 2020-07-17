package com.microwu.cxd.spring.json;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   10:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClassPathJsonApplicationContext extends AbstractRefreshableConfigApplicationContext {
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory defaultListableBeanFactory) throws BeansException, IOException {
        // 用给定的 beanFactory 创建一个 JsonBeanDefinitionReader
        JsonBeanDefinitionReader beanDefinitionReader = new JsonBeanDefinitionReader(defaultListableBeanFactory);

        // 配置Bean definition reader
        beanDefinitionReader.setEnvironment(this.getEnvironment());
        beanDefinitionReader.setResourceLoader(this);

        loadBeanDefinitions(beanDefinitionReader);
    }

    protected void loadBeanDefinitions(JsonBeanDefinitionReader reader) {
        String[] configResources = getConfigLocations();
        if (configResources != null) {
            reader.loadBeanDefinitions(configResources);
        }
    }

    public ClassPathJsonApplicationContext(String configLocation) {
        this(new String[] {configLocation}, true, null);
    }

    public ClassPathJsonApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent) {
        super(parent);

        setConfigLocations(configLocations);
        if (refresh) {
            refresh();
        }
    }
}