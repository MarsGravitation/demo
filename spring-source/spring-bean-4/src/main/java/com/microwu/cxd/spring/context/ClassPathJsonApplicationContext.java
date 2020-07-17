package com.microwu.cxd.spring.context;

import com.microwu.cxd.spring.JsonBeanDefinitionReader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   18:03
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClassPathJsonApplicationContext extends AbstractRefreshableConfigApplicationContext {
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory defaultListableBeanFactory) throws BeansException, IOException {
        // 其实主要内容和 xmlApplicationContext 一样的
        JsonBeanDefinitionReader beanDefinitionReader = new JsonBeanDefinitionReader(defaultListableBeanFactory);

        beanDefinitionReader.setEnvironment(this.getEnvironment());
        beanDefinitionReader.setResourceLoader(this);

        // 通过 json bean definition reader 去读取 bean definition
        loadBeanDefinitions(beanDefinitionReader);
    }

    protected void loadBeanDefinitions(JsonBeanDefinitionReader reader) {
        // 这里获取 json 文件的 path， 这个 location 是 new ClassPathJsonApplicationContext 时传进来的
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            reader.loadBeanDefinitions(configLocations);
        }
    }

    public ClassPathJsonApplicationContext(String configLocation) {
        this(new String[]{configLocation}, true, null);
    }

    /**
     * 这里一模一样，不需要任何变化
     *
     * @param configLocations
     * @param refresh
     * @param parent
     */
    public ClassPathJsonApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent) {
        super(parent);
        setConfigLocations(configLocations);
        if (refresh) {
            refresh();
        }
    }
}