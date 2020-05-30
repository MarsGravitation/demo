package com.microwu.cxd.dubbo.provider;

import com.microwu.cxd.dubbo.provider.config.DubboConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/21   10:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ProviderApp {
    public static void main(String[] args) throws IOException {
//        // 加载xml 配置文件启动
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/provider.xml");
//        context.start();
//
//
//        // api 方式配置
//        ProviderService providerService = new ProviderServiceImpl();
//
//        // 当前应用配置
//        ApplicationConfig applicationConfig = new ApplicationConfig();
//        applicationConfig.setName("provider");
//        applicationConfig.setOwner("cxd");
//
//        // 连接注册中心
//        RegistryConfig registryConfig = new RegistryConfig();
//        registryConfig.setAddress("zookeeper://192.168.133.134:2181");
//
//        // 服务提供者协议配置
//        ProtocolConfig protocolConfig = new ProtocolConfig();
//        protocolConfig.setName("dubbo");
//        protocolConfig.setPort(20880);
//
//        // ServiceConfig 为重对象，内部封装了与注册中心的连接，以及开启服务端口
//        ServiceConfig<ProviderService> serviceConfig = new ServiceConfig<ProviderService>();
//        serviceConfig.setApplication(applicationConfig);
//        serviceConfig.setRegistry(registryConfig);
//        serviceConfig.setProtocol(protocolConfig);
//        serviceConfig.setInterface(ProviderService.class);
//        serviceConfig.setRef(providerService);
//        serviceConfig.setVersion("1.0.0");
//
//        // 暴露及注册服务
//        serviceConfig.export();

        // 注解启动方式
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DubboConfiguration.class);
        context.start();

        // 按任意键退出
        System.in.read();
    }
}