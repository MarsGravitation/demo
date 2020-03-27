package com.microwu.cxd.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Feign 是一个声明式地伪Http 客户端, 它使得写http客户端变得更简单. 使用Feign, 只需要创建一个接口并注解.
 * 它具有可插拔的注解特性, 可使用Feign 注解和 JAX-RS注解. Feign 支持可插拔的编码器和解码器.
 * Feign 默认继承了Ribbon, 并和Eureka 结合, 默认实现了负载均衡
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceFeignApplication
{
    public static void main( String[] args )
    {

        SpringApplication.run(ServiceFeignApplication.class, args);
    }
}
