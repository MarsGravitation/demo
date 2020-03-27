package com.microwu.cxd.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 启动一个服务注册中心, 只需要一个注解 @EnableEurekaServer
 *
 * eureka 是一个高可用的组件, 它没有后端缓存, 每一个实例组册之后都需要向注册中心
 * 发送心跳, 默认情况下, Server 也是 client, 必须制指定一个server
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication
{
    public static void main( String[] args)
    {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
