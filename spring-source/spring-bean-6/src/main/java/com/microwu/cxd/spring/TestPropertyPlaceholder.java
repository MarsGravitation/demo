package com.microwu.cxd.spring;

import com.microwu.cxd.spring.properties.TestPropertiesBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/2   14:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TestPropertyPlaceholder {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"context-namespace-test-property-holder.xml"}, true);

        TestPropertiesBean bean = context.getBean(TestPropertiesBean.class);
        System.out.println(bean);
    }
}