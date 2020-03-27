package com.microwu.cxd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 从Dalston以后，不需要添加 @EnableDiscoveryClient或 @EnableEurekaClient
 * 可以省略
 *
 */
@SpringBootApplication
@RestController
public class ProviderUserApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ProviderUserApplication.class, args);
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hi, i am user1";
    }
}
