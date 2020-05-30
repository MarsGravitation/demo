package com.microwu.cxd.dubbo.consumer;

import com.microwu.cxd.dubbo.consumer.annotation.ConsumerAnnotationService;
import com.microwu.cxd.dubbo.consumer.config.DubboConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/21   10:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConsumerApp {
    public static void main(String[] args) throws IOException {

////        xml 配置方式
//        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("consumer.xml");
//        context.start();
//        ProviderService providerService = (ProviderService) context.getBean("providerService");
//        String str = providerService.sayHello("world");
//        System.out.println(str);

//        // API 配置
//        // 当前应用配置
//        ApplicationConfig applicationConfig = new ApplicationConfig();
//        applicationConfig.setName("consumer");
//        applicationConfig.setOwner("cxd");
//
//        // 连接注册中心配置
//        RegistryConfig registryConfig = new RegistryConfig("zookeeper://192.168.133.134:2181");
//
//        // 引用远程服务
//        ReferenceConfig<ProviderService> referenceConfig = new ReferenceConfig<ProviderService>();
//        referenceConfig.setApplication(applicationConfig);
//        referenceConfig.setRegistry(registryConfig);
//        referenceConfig.setInterface(ProviderService.class);
//
//        // 调用服务
//        // 此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
//        ProviderService providerService = referenceConfig.get();
//        providerService.sayHello("world!!!");
//        System.in.read();

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DubboConfiguration.class);
        context.start();

        ConsumerAnnotationService service = context.getBean(ConsumerAnnotationService.class);
        String world = service.sayHello("world");
        System.out.println("hello " + world);

    }
}