package com.microwu.reflex.introspector;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Description:
 *  1. Introspector
 *      类似于 BeanInfo 的静态工厂类，主要是提供静态方法通过 Class 实例获取 BeanInfo
 *  2. BeanInfo
 *      getBeanDescriptor - 获取 JavaBean 描述符
 *      getPropertyDescriptors - 获取 JavaBean 的所有的 PropertyDescriptor
 *      getMethodDescriptors - 获取 javaBean 的所有 MethodDescriptor
 *
 *      获取所有的描述时，还会带有一个属性名为 class 的实例，如果不需要，需要过滤
 *  3. PropertyDescriptor
 *      getPropertyType - 获取属性的 Class 对象
 *      getReadMethod
 *      getWriteMethod
 *      setReadMethod
 *      setWriteMethod
 *
 *  4. 不正当使用会导致内存溢出
 *      spring 中通过 CachedIntrospectionResults 自行管理
 *
 * https://mp.weixin.qq.com/s?__biz=MzU4MDYxNjQ1OQ==&mid=2247486194&idx=1&sn=d50d4424e90dd4ee14ba33ad586eb3e4&chksm=fd556296ca22eb80a957172770e32659946d7eab92534e031d097e4e5bec3b767bc86b1a3595&scene=126&sessionid=1597107225&key=2008e9df245ac54d2577f125d90e7877e56314a79afa5b34cb54966576561819a718988ff054e8ba38ddb3b1ea2fe4e48609379f6e795bdeebf4ca8fb2bcc2dabd21bf9ce42262c378e7b5b47f45366f&ascene=1&uin=MTAwMTU2MDQyOA%3D%3D&devicetype=Windows+10+x64&version=62090538&lang=zh_CN&exportkey=A75KqjjAHrRV45vyDjtYVr8%3D&pass_ticket=BeqjOMuhXy%2FVuOWjhsdlSG3h2NnueJrVzyTmPOUxg%2BqVyUEUc%2FQ4HZZb7QTXYim4
 * https://www.cnblogs.com/peida/archive/2013/06/03/3090842.html
 * https://blog.csdn.net/choushi9178/article/details/100747262
 * https://www.jianshu.com/p/220512f4b2d4
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/11   9:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class IntrospectorMain {

    public static void main(String[] args) throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(Person.class);
        // 获取所有的描述符
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (!"class".equalsIgnoreCase(propertyDescriptor.getName())) {
                System.out.println(propertyDescriptor.getName());
                System.out.println(propertyDescriptor.getWriteMethod().getName());
                System.out.println(propertyDescriptor.getReadMethod().getName());
                System.out.println("========================");
            }
        }

        Person person = Person.class.newInstance();
        // 用于获取单个属性
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor("name", Person.class);
        Method writeMethod = propertyDescriptor.getWriteMethod();
        writeMethod.invoke(person, "cxd");
        System.out.println(person);

    }

    public static class Person {
        private Long id;
        private String name;
        private Integer age;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}