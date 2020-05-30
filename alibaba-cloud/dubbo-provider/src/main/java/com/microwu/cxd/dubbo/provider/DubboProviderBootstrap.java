package com.microwu.cxd.dubbo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/28   14:19
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@EnableDiscoveryClient
@EnableAutoConfiguration
public class DubboProviderBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(DubboProviderBootstrap.class, args);
    }
}