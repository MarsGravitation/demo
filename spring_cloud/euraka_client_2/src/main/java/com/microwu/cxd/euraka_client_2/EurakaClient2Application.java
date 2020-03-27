package com.microwu.cxd.euraka_client_2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurakaClient2Application {
    @Value("${server.port}")
    private String port;

    @RequestMapping("hi")
    @ResponseBody
    public String sayHi(@RequestParam String name){
        return "i am " + name + ", i am from " + port;
    }

    public static void main(String[] args) {
        SpringApplication.run(EurakaClient2Application.class, args);
    }

}

