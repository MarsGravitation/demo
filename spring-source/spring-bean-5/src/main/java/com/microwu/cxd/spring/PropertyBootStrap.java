package com.microwu.cxd.spring;

import java.util.Map;

/**
 * Description: 从 properties 文件中读取 bean definition
 *      这里 spring 帮我们实现了，我们只需要定义一个 ApplicationContext 就可以
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/2   10:22
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PropertyBootStrap {
    public static void main(String[] args) {
        ClassPathPropertyFileApplicationContext context = new ClassPathPropertyFileApplicationContext("beanDefinition.properties");

        Map<String, Employee> beansOfType = context.getBeansOfType(Employee.class);
        for (Map.Entry<String, Employee> entry : beansOfType.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}