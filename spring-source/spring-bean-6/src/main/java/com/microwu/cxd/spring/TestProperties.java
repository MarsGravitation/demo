package com.microwu.cxd.spring;

import com.microwu.cxd.spring.properties.TestPropertiesBean;
import lombok.Data;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Iterator;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/2   14:17
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class TestProperties {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("util-namespace-test-properties.xml");

        Iterator<String> beanNamesIterator = context.getBeanFactory().getBeanNamesIterator();
        while (beanNamesIterator.hasNext()) {
            String next = beanNamesIterator.next();
//            System.out.println(context.getBeanFactory().getBeanDefinition(next));
            System.out.println(next);
        }

        TestPropertiesBean bean = context.getBean(TestPropertiesBean.class);
        System.out.println(bean);
    }
}