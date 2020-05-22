package com.microwu.cxd.spring;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/20   12:31
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyApp {
    public static void main(String[] args) {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new ClassPathResource("spring-config.xml"));
        MyTestBean myTestBean = (MyTestBean) xmlBeanFactory.getBean("myTestBean");
        System.out.println(myTestBean.getName());

    }
}