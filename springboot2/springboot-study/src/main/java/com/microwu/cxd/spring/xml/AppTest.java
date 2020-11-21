package com.microwu.cxd.spring.xml;

import com.microwu.cxd.spring.domain.MyTestBean;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/9   15:11
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AppTest {

    public static void main(String[] args) {
        XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("spring-config.xml"));
        MyTestBean bean = beanFactory.getBean(MyTestBean.class);
        System.out.println(bean.getName());
    }

}