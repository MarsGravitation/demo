package com.microwu.cxd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableFeignClients
public class ConsumerFeignApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ConsumerFeignApplication.class, args);
    }
}
