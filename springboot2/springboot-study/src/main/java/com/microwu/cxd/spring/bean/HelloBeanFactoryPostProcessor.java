package com.microwu.cxd.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Description:
 *  BeanPostProcessor 回调对应的主体是 bean，而 BeanFactoryPostProcessor 的主体是 BeanFactory
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/9   10:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HelloBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    /**
     * 在 ApplicationContext 内部的 BeanFactory 加载完 bean 的定义，但是在对应的 bean 实例化之前进行回调
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/9  10:16
     *
     * @param   	beanFactory
     * @return  void
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 获取所有的 beanName
        String[] names = beanFactory.getBeanDefinitionNames();
        if (names != null && names.length > 0) {
            BeanDefinition beanDefinition = null;
            for (String name : names) {
                // 获取对应的 bean 定义
                beanDefinition = beanFactory.getBeanDefinition(name);
                this.printBeanDef(name, beanDefinition);
            }
        }
    }

    private void printBeanDef(String name, BeanDefinition beanDefinition) {
        StringBuilder defStr = new StringBuilder("beanName: ").append(name);
        defStr.append(", className: ").append(beanDefinition.getBeanClassName());
        defStr.append(", scope: ").append(beanDefinition.getScope());
        defStr.append(", parent: ").append(beanDefinition.getParentName());
        defStr.append(", factoryBean: ").append(beanDefinition.getFactoryBeanName());
        defStr.append(", factoryMethod: ").append(beanDefinition.getFactoryMethodName());
        System.out.println(defStr);
    }
}