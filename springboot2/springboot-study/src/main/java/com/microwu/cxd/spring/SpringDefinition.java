package com.microwu.cxd.spring;

import lombok.Data;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * Description: bean definition
 *  spring IOC 容器中装的似乎 bean
 *  bean 的特征： bean 是一个对象，有名字，class 类型，scope， role 等等有关 bean 的属性
 *
 *  bean definition 有很多实现类
 *      1. AnnotatedBeanDefinition，扩展了BeanDefinition，可以获得bean definition 中的bean class 上的注解元数据
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:                   2020/5/30   8:30
 * Copyright:              北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * <p>
 * Author        Time            Content
 */
@Data
public class SpringDefinition {

    /**
     * bean class 名
     */
    String beanClassName;

    /**
     * 工厂 bean 的名称
     */
    String factoryBeanName;

    /**
     * 工厂方法的名称
     */
    String factoryMethodName;

    /**
     * singleton/prototype
     */
    String scope;

    /**
     * 是否延迟初始化
     */
    boolean isLazyInit;

    /**
     * bean 的角色，比如：1. 框架 2. 应用
     */
    int role;

    /**
     * 是否为主候选 bean
     */
    boolean primary;

//    BeanDefinition

}