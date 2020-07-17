package com.microwu.cxd.spring.controller;

import com.microwu.cxd.spring.service.TestService;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 手动注册bean Definition
 * 大体思路：
 *  1. 选择bean definition 实现类，并实例化bean definition
 *  2. 注册bean definition
 *  3. get bean 查看是否work
 *
 *  1. 选择bean definition 实现类
 *      BeanDefinition 只是一个接口，GenericBeanDefinition 官方推荐的
 *      GenericBeanDefinition 无参构造，调用了父类的无参构造
 *      它主要设置了一些属性的默认值
 *
 *  2. 注册bean definition
 *      DefaultListableBeanFactory 实现了ConfigurableListableBeanFactory， 还实现了BeanDefinitionRegistry
 *
 *  3. 如何表达bean 间的依赖
 *      构造器注入和 property 注入
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   9:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
@NoArgsConstructor
public class ControllerByConstructor {

    TestService testService;

    /**
     * 基本类型依赖
     */
    private String name;

    public ControllerByConstructor(TestService testService, String name) {
        this.testService = testService;
        this.name = name;
    }

    public ControllerByConstructor(String name) {
        this.name = name;
    }

}