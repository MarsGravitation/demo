package com.microwu.cxd.spring;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *  1. 添加依赖
 *  2. 添加 @EnableNacosConfig 注解启用 Nacos Spring 的配置服务
 * @NacosPropertySource 加载了 dataId = example 的配置源，并开启自动更新
 *  3. 通过 Nacos 的 @NacosValue 注解设置属性值
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/17   15:14
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootApplication
@Configuration
@EnableNacosConfig(globalProperties = @NacosProperties(serverAddr = "192.168.133.134:8848"))
@NacosPropertySource(dataId = "example", autoRefreshed = true)
public class NacosConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(NacosConfiguration.class, args);
    }
}