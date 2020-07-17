package com.microwu.cxd.spring;

/**
 * Description: bean definition
 *      Spring 主打ioc 容器，容器里面装了 bean
 *      bean 有啥特征，bean 是一个对象，有名字，有 class 类型，有 scope， 有 role（属于应用的 bean， 还是 spring 框架的bean），
 *      是否延迟初始化，有其他依赖的bean
 *
 *      org.springframework.beans.factory.config.BeanDefinition 接口：描述了一个bean 实例的各种属性，由其声明了：这是一个最小化接口，
 *      主要目的是允许bean factory 后置处理器对 bean property 和其他元数据进行修改
 *
 *  接口：AnnotatedBeanDefinition - 可以获取到 bean definition 中对应bean class 标注的注解元数据，这个测试过了，这里不测试了
 *
 *  AbstractBeanDefinition 充当了基本的实现
 *  GenericBeanDefinition
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   16:23
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SpringDefinition {

    /**
     * bean class
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
     * 依赖的bean
     */
    String[] dependsOn;

    /**
     * bean 的角色，比如：1、框架 2、应用
     */
    int role;

    /**
     * 是否为主候选 bean
     */
    boolean primary;
}