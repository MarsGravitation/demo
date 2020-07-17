package com.microwu.cxd.spring;

/**
 * Description:
 *  1. org.springframework.beans.factory.config.BeanDefinition 这里面的属性就不详细介绍的
 *  2. beanName: org.springframework.beans.factory.support.DefaultListableBeanFactory
 *      private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
 *      采用map 存储bean 和BeanDefinition，key 是 beanName，默认规则是 beanClassName 按照驼峰转换后的名字
 *
 *      如果同一个上下文中，有两个beanName 相同的 BeanDefinition?
 *      - 启动报错，spring boot  allow-bean-definition-overriding 改成了 false
 *
 *      - scope：默认 singleton
 *      - parentName：XML 用的比较多一点，注解驱动，基本很少用了
 *      - beanClassName：核心属性，bean 的 class 类型，这里说的是实际类型，而不是接口的名称
 *      - factoryBeanName, factoryMethodName：如果本 bean 是通过其他工厂bean 来创建的，则这两个字段为对应的工厂bean 的名称和对应工厂方法的名称
 *      - lazyInit：是否延迟初始化
 *      - dependsOn：通过这个属性设置的bea，会先于本 bean 被初始化
 *      - autowireCandidate：表示是否可以被其他的bean 使用 @autowired 的方式注入
 *      - primary：当由多个候选 bean 满足 @autowired 要求时，其中 primary 被设置为 true
 *      - constructorArgumentValues：构造函数属性值，需要使用 XML 配置，这里我没测试
 *      - propertyValues： property 方式注入时的属性值，set 方法注入
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   16:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BeanDefinitionDemo {
    public static void main(String[] args) {

    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}