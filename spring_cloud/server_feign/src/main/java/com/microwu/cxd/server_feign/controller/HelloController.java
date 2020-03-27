package com.microwu.cxd.server_feign.controller;

import com.microwu.cxd.server_feign.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private HelloService helloService;

    @RequestMapping("hi")
    @ResponseBody
    public String sayHello(String name){
        return helloService.helloService(name);
    }
}
