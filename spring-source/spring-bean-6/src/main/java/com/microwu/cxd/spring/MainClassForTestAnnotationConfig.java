package com.microwu.cxd.spring;

import com.microwu.cxd.spring.controller.TestController;
import com.microwu.cxd.spring.service.TestService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description: @Autowired 解析
 *  1. 实际 @Autowired 并不在 TestController 的 beanDefinition 中，而是在 getBean 的时候，由 AutowiredAnnotationBeanPostProcessor 来获取的
 *  2. 在 getBean 的时候，先去创建 bean，之后调用 autowired 这个 BeanPostProcessor 来动态构造的
 *  3. 在 bean 创建完，但是属性还是没设置时，框架去调用 BeanPostProcessor，导致AutowiredAnnotationBeanPostProcessor#postProcessPropertyValues 被调用
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/2   14:55
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MainClassForTestAnnotationConfig {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context-namespace-test-annotation-config.xml");

        TestService service = context.getBean(TestService.class);
        TestController controller = context.getBean(TestController.class);
        System.out.println(controller + "\r\n" + service);

//        AnnotationConfigBeanDefinitionParser
    }
}