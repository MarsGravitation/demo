package com.microwu.cxd.bean.bootstrap;

import com.microwu.cxd.bean.pojo.MyTestBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/12   10:53
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BootstrapTest {

    public static void main(String[] args) {
        // 使用 BeanFactory 加载 XML
//        XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("spring-config.xml"));
//        System.out.println(beanFactory.getBean(MyTestBean.class));
//        System.out.println(beanFactory.getBean(MyTestBean.class));

        // 使用 ApplicationContext 加载 XML
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        MyTestBean bean = context.getBean(MyTestBean.class);
        System.out.println(bean);
//
//        Car car = context.getBean(Car.class);
//        System.out.println(car);

//        System.out.println(context.getBean(MyTestBean.class));

//        MyTestBean2 bean2 = context.getBean(MyTestBean2.class);
//        System.out.println(bean2);
    }
}