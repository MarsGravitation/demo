package com.microwu.cxd.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Description: BeanPostProcessor 是 Spring 中定义的一个接口。Spring 将在初始化 bean 前后对 BeanPostProcessor 实现类进行回调，所有的 bean
 *  初始化前后都会回调 BeanPostProcessor 实现类，而 InitializingBean 或者 DisposableBean 是针对单个 bean。
 *
 *  BeanPostProcessor 只需要把它当成一个普通的 bean 定义到 Spring 的容器中，Spring 能够自动检测到，并将它注册到当前的 bean 容器中。
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/9   10:01
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HelloBeanPostProcessor implements BeanPostProcessor {

    /**
     * 在一个 bean 完全初始化后进行回调，此时对应的依赖注入已经完成
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/9  10:06
     *
     * @param   	bean
     * @param 		beanName
     * @return  java.lang.Object
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 在一个 bean 被完全初始化前进行回调，此时对应的 bean 已经实例化，但是对应的属性注入还没有进行
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/9  10:05
     *
     * @param   	bean
     * @param 		beanName
     * @return  java.lang.Object
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("beanName -------" + beanName);
        return bean;
    }
}