package com.microwu.cxd.dubbo.provider.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/26   11:14
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
@EnableDubbo(scanBasePackages = "com.microwu.cxd.dubbo.provider.annotation")
public class DubboConfiguration {

    /**
     * 服务提供者信息配置
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/26  11:17
     *
     * @param
     * @return  com.alibaba.dubbo.config.ProviderConfig
     */
    @Bean
    public ProviderConfig providerConfig() {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(1000);
        return providerConfig;
    }

    /**
     * 分布式应用信息配置
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/26  11:18
     *
     * @param
     * @return  com.alibaba.dubbo.config.ApplicationConfig
     */
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("dubbo-annotation-provider");
        return applicationConfig;
    }

    /**
     * 注册中心信息配置
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/26  11:20
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

    /**
     * 协议配置
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/26  11:21
     *
     * @param
     * @return  com.alibaba.dubbo.config.ProtocolConfig
     */
    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(20880);
        return protocolConfig;
    }
}