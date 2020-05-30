package com.microwu.cxd.dubbo.consumer.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/26   11:25
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
@EnableDubbo(scanBasePackages = "com.microwu.cxd.dubbo.consumer.annotation")
@ComponentScan(value = {"com.microwu.cxd.dubbo.consumer.annotation"})
public class DubboConfiguration {

    /**
     * 应用配置
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/26  11:28
     *
     * @param
     * @return  com.alibaba.dubbo.config.ApplicationConfig
     */
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("dubbo-annotation-consumer");
        return applicationConfig;
    }

    /**
     * 服务消费者配置
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/26  11:29
     *
     * @param
     * @return  com.alibaba.dubbo.config.ConsumerConfig
     */
    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setTimeout(3000);
        return consumerConfig;
    }

    /**
     * 配置注册中心
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/26  11:30
     *
     * @param
     * @return  com.alibaba.dubbo.config.RegistryConfig
     */
    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol("zookeeper");
        registryConfig.setAddress("192.168.133.134");
        registryConfig.setPort(2181);
        return registryConfig;
    }
}