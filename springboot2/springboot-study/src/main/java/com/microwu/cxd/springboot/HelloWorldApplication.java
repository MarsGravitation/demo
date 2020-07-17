package com.microwu.cxd.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description:
 * @SpringBootApplication 是一个组合注解，标注在某个类上，说明这个类是SpringBoot 的主配置类
 * SpringBoot 应该运行这个类的main 方法来启动SpringBoot 应用
 *
 *  1. @SpringBootConfiguration: 其实就是Configuration 配置类，意思是HelloWorldApplication 会被注册到Spring容器中
 *  2. @EnableAutoConfiguration: 开启自动配置功能；以前使用Spring需要配置的信息，SpringBoot 帮助自动配置；
 *      > @AutoConfigurationPackage: 自动配置包注解
 *      > @Import(AutoConfigurationPackages.Registrar.class): 默认将主配置类 @SpringBootApplication 所在的包及其子包里面的所有组件扫描到Spring容器中
 *  3. @Import(EnableAutoConfigurationImportSelector.class): 导入那些组件的选择器，将所有需要导入的组件以全类名的方式返回，这些组件就会被添加到容器中
 *      SpringBoot 启动的时候从类路径下的META-INF/spring.factories 中获取EnableAutoConfiguration指定的值，病假这些值作为自动配置类导入容器中，自动配置类就会生效
 *      最后完成自动配置工作
 *      默认在spring-boot-autoconfigure 这个包中
 *
 *      J2EE的整体整合解决方案和自动配置都在spring-boot-autoconfigure-xxx.jar 包中。在这些自动配置类中会通过@ConditionalOnClass 等条件注解判断是否导入了某些依赖包，
 *      从而通过@Bean 注册相应的对象进行自动配置
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/29   13:59
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootApplication(scanBasePackages = {"com.microwu.cxd"})
public class HelloWorldApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);
    }
}