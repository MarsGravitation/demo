package com.microwu.cxd.spring;

import com.microwu.cxd.spring.controller.TestController;
import com.microwu.cxd.spring.service.TestService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Iterator;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/2   15:27
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MainClassForTestComponentScan {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context-namespace-test-component-scan.xml");

        Iterator<String> beanNamesIterator =
                context.getBeanFactory().getBeanNamesIterator();

        while (beanNamesIterator.hasNext()) {
            System.out.println(beanNamesIterator.next());
        }
        TestService service = context.getBean(TestService.class);
        TestController controller = context.getBean(TestController.class);
        System.out.println(controller + "\r\n" + service);

//        ContextNamespaceHandler
//        ComponentScanBeanDefinitionParser
//        ClassPathBeanDefinitionScanner
    }
}