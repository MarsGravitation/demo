package com.microwu.cxd.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 在微服务架构中, 业务都会被拆分成一个独立的服务, 服务与服务的通讯是基于http RESTful的
 * Spring Cloud 有两种服务调用方式, 一种是ribbon + restTemplate, 另一种是 feign
 *
 * 通过 @EnableDiscoveryClient 向服务中心注册, 向容器注入restTemplate, 并开启负载均衡
 *
 * 此时架构:
 *  1. 一个服务注册中心
 *  2. 两个客户端, service-hi, 向服务注册中心注册
 *  3. 访问ribbon 进行负载均衡
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableHystrix
public class ServiceRibbonApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ServiceRibbonApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
